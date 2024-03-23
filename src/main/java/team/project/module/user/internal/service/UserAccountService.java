package team.project.module.user.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRole;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.tmp.service.DepartmentService;

/*
 * 2024-03-23
 * 关于注册，先不考虑验证学号/工号的正确性
 * 因为学校未提供合适的接口，我们也做不到验证
 * 如果学校提供了接口，我们可以通过学号/工号判断注册者的身份（是学生或是教师）
 * 现在，我们无法避免下述情况的恶意注册：
 *    用别人的（或是不存在的）学号/工号注册
 *    学生（教师）注册了用户，但选择用户的身份是教师（学生）
 *
 * 关于注销，我们只是将用户账号逻辑删除，数据库中保留有用户信息（包括学号/工号）
 * 由于学号/工号的“唯一键”约束，所以用户无法注册相同学号/工号的账号
 * 我们期望的办法是：用户与管理员联系，撤销逻辑删除
 * 但是，正如上方所说，我们无法保证用户拿别人的（或是不存在的）学号/工号注册新账号
 * */


@Service
public class UserAccountService {

    @Autowired
    TblUserMapper userMapper;

    @Autowired
    DepartmentService departmentService;

    public void register(RegisterReq req) {
        TblUserDO user = new TblUserDO();

        user.setUserId(req.getUserId());
        if ( ! departmentService.isDepartmentExist(req.getDepartmentId())) {
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "院系不存在");
        }

        user.setDepartmentId(req.getDepartmentId());
        user.setPassword(req.getPassword()); /* <- TODO: 待加密 */
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

        userMapper.insert(user);
    }

    public boolean login(String userId, String password) {
        return userMapper.isExist(userId, password);
    }

    public void cancelAccount(String userId, String password) {
        int result = userMapper.logicalDelete(userId, password);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.UNAUTHORIZED, "账号不存在或密码错误");
        }
    }
}
