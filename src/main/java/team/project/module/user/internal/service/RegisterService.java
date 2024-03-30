package team.project.module.user.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRoleEnum;
import team.project.module.user.internal.model.request.RegisterReq;

@Service
public class RegisterService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TblUserMapper userMapper;

    public void register(RegisterReq req) {
        TblUserDO user = new TblUserDO();

        user.setUserId(req.getUserId());

        /* 2024-03-29 ljh
           注册账号时要指明所属院系，可以在这里判断院系是否存在。即使这里不判断，在下方执行插入 sql 时也会判断
           因为数据库表设计中，对“用户所属院系”字段设有“外键”约束。此外，还对“学号/工号”字段设有“唯一键”约束
           所以索性这里也不判断院系了
        if ( ! departmentService.isDepartmentExist(req.getDepartmentId())) {
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "注册失败");
        } */

        user.setDepartmentId(req.getDepartmentId());
        user.setPassword(req.getPassword()); /* <- TODO: 待加密 */
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setTel(req.getTel());

        user.setRole(UserRoleEnum.getEmptyRoleCode());
        if ("student".equals(req.getRole())) {
            user.addRole(UserRoleEnum.STUDENT);
        }
        else if ("teacher".equals(req.getRole())) {
            user.addRole(UserRoleEnum.TEACHER);
        }
        else {
            assert false : "controller 的入参校验保证程序不会执行到此处";
        }

        try {
            userMapper.insert(user);
        }
        catch (Exception e) {
            logger.error("注册失败：", e); /* 可能是触发“唯一键”或“外键”约束 */
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "注册失败");
        }
    }

    public void unregister(String userId, String password) {
        int result = userMapper.logicalDelete(userId, password);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.UNAUTHORIZED, "账号不存在或密码错误");
        }
    }
}
