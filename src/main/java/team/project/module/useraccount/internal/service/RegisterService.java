package team.project.module.useraccount.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.useraccount.internal.mapper.TblUserMapper;
import team.project.module.useraccount.internal.model.entity.TblUserDO;
import team.project.module.useraccount.internal.model.request.RegisterReq;
import team.project.module.useraccount.tmp.service.DepartmentService;

@Service
public class RegisterService {

    @Autowired
    TblUserMapper tblUserMapper;

    @Autowired
    DepartmentService departmentService;

    public void register(RegisterReq req)
    {
        TblUserDO user = new TblUserDO();
        user.setUserId(req.getUserId());
        if ( ! departmentService.isDepartmentExist(req.getDepartmentId())) {
            throw new ServiceException(ServiceStatus.BAD_REQUEST, "院系不存在");
        }
        user.setDepartmentId(req.getDepartmentId());
        user.setPassword(req.getPassword()); /* <- 待加密 */
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setTel(req.getTel());
        if ("student".equals(req.getRole())) {
            user.addRole(TblUserDO.Role.ROLE_STUDENT);
        } else if ("teacher".equals(req.getRole())) {
            user.addRole(TblUserDO.Role.ROLE_TEACHER);
        } else {
            throw new ServiceException(ServiceStatus.BAD_REQUEST, "未指定用户身份是学生或教师");
        }
        tblUserMapper.insert(user);
    }
}
