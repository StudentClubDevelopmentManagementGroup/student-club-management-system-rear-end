package team.project.module.user.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRole;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.tmp.service.DepartmentService;

@Service
public class RegisterService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TblUserMapper tblUserMapper;

    @Autowired
    DepartmentService departmentService;

    public void register(RegisterReq req)
    {
        TblUserDO user = new TblUserDO();

        user.setUserId(req.getUserId());
        if ( ! departmentService.isDepartmentExist(req.getDepartmentId())) {
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "院系不存在");
        }

        user.setDepartmentId(req.getDepartmentId());
        user.setPassword(req.getPassword()); /* <- 待加密 */
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
            assert false : "controller 的入参校验保证程序不会执行到此处";
        }

        tblUserMapper.insert(user);
    }
}
