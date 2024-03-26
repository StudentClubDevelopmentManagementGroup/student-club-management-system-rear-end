package team.project.module.user.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRole;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.internal.model.view.PageVO;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.tmp.service.DepartmentService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccountService {

    @Autowired
    TblUserMapper userMapper;

    @Autowired
    DepartmentService departmentService;

    UserInfoVO toUserInfoVO(TblUserDO user) {
        UserInfoVO.Role role = new UserInfoVO.Role();
        role.setStudent(user.hasRole(UserRole.STUDENT));
        role.setTeacher(user.hasRole(UserRole.TEACHER));
        role.setClubManager(user.hasRole(UserRole.CLUB_MANAGER));
        role.setSuperAdmin(user.hasRole(UserRole.SUPER_ADMIN));

        UserInfoVO.Department department = new UserInfoVO.Department();
        department.setDepartmentId(user.getDepartmentId());
        department.setDepartmentName(departmentService.getNameById(user.getDepartmentId()));

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(user.getUserId());
        userInfo.setDepartment(department);
        userInfo.setName(user.getName());
        userInfo.setTel(user.getTel());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(role);

        return userInfo;
    }

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

        userMapper.insert(user); /* TODO: 捕获因插入不满足“唯一键”约束抛出的异常 */
    }

    public UserInfoVO login(String userId, String password) {
        TblUserDO user = userMapper.selectOne(userId, password);
        if (user == null) {
            throw new ServiceException(ServiceStatus.UNAUTHORIZED, "用户名或密码错误");
        }

        /*UserInfoVO.Role role = new UserInfoVO.Role();
        role.setStudent(user.hasRole(UserRole.STUDENT));
        role.setTeacher(user.hasRole(UserRole.TEACHER));
        role.setClubManager(user.hasRole(UserRole.CLUB_MANAGER));
        role.setSuperAdmin(user.hasRole(UserRole.SUPER_ADMIN));

        UserInfoVO.Department department = new UserInfoVO.Department();
        department.setDepartmentId(user.getDepartmentId());
        department.setDepartmentName(departmentService.getNameById(user.getDepartmentId()));

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(userId);
        userInfo.setDepartment(department);
        userInfo.setName(user.getName());
        userInfo.setTel(user.getTel());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(role);

        return userInfo;*/

        return toUserInfoVO(user);
    }

    public void cancelAccount(String userId, String password) {
        int result = userMapper.logicalDelete(userId, password);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.UNAUTHORIZED, "账号不存在或密码错误");
        }
    }

    public PageVO<UserInfoVO> selectAll(int pageNum, int pageSize) {
        Page<TblUserDO> page = new Page<>(pageNum, pageSize, true);
        List<TblUserDO> userList = userMapper.selectList(page, null);
        List<UserInfoVO> userInfoList = new ArrayList<>();
        for (TblUserDO user : userList) {
            userInfoList.add(toUserInfoVO(user));
        }
        return new PageVO<>(userInfoList, page);
    }
}

