package team.project.module.user.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.internal.dao.UserDAO;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.request.RegisterReq;

@Service
public class RegisterService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDAO userDAO;

    public void register(RegisterReq req) {
        UserDO user = new UserDO();

        user.setUserId(req.getUserId());

        /* 2024-03-29 ljh
           注册账号时要指明所属院系，可以在这里判断院系是否存在。即使这里不判断，在下方执行插入 sql 时也会判断
           因为数据库表设计中，对“用户所属院系”字段设有“外键”约束。此外，还对“学号/工号”字段设有“唯一键”约束
           所以索性这里也不判断院系了
        if ( ! departmentService.isDepartmentExist(req.getDepartmentId())) {
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "注册失败");
        } */

        user.setDepartmentId(req.getDepartmentId());
        user.setPassword(req.getPassword()); /* <- ljh_TODO: 待加密 */
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setTel(req.getTel());

        user.setRole(UserRole.getEmptyRoleCode());
        if ("student".equals(req.getRole())) {
            user.addRole(UserRole.STUDENT);
        }
        else if ("teacher".equals(req.getRole())) {
            user.addRole(UserRole.TEACHER);
        }
        else {
            /* controller 的入参校验保证程序不会执行到此处 */
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "不支持用户创建这个角色的账户");
        }

        try {
            userDAO.insert(user);
        }
        catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                /* 2024-03-31 依目前的表设计，只有”学号/工号已存在“会触发”唯一键“异常 */
                logger.info("用户注册失败：（可能是因为学号/工号已存在？）", e);
            }
            else if (e instanceof DataIntegrityViolationException) {
                /* 2024-04-10 依目前的表设计，只有”院系id不存在“会触发”外键“异常 */
                logger.info("用户注册失败：（可能是因为外键院系id不存在？）", e);
            }
            else {
                logger.error("用户注册失败：", e);
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "注册失败");
            }
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "注册失败");
        }
    }

    public void unregister(String userId, String password) {
        int result = userDAO.logicalDelete(userId, password);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.UNAUTHORIZED, "账号不存在或密码错误");
        }
    }
}
