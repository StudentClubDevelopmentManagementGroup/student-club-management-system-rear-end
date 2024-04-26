package team.project.module.user.internal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRoleEnum;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.tmp.service.DepartmentService;

@Component("user-util-ModelConverter")
public class ModelConverter {

    @Autowired
    DepartmentService departmentService; /* ljh_TODO：取消对 tmp 模块的依赖 */

    public UserInfoVO toUserInfoVO(TblUserDO userDO) {

        UserInfoVO.UserRoleInfo role = new UserInfoVO.UserRoleInfo();
        role.setStudent(userDO.hasRole(UserRoleEnum.STUDENT));
        role.setTeacher(userDO.hasRole(UserRoleEnum.TEACHER));
        role.setClubMember(userDO.hasRole(UserRoleEnum.CLUB_MEMBER));
        role.setClubManager(userDO.hasRole(UserRoleEnum.CLUB_MANAGER));
        role.setSuperAdmin(userDO.hasRole(UserRoleEnum.SUPER_ADMIN));

        UserInfoVO.DepartmentInfo department = new UserInfoVO.DepartmentInfo();
        department.setDepartmentId(userDO.getDepartmentId());
        department.setDepartmentName(departmentService.getNameById(userDO.getDepartmentId()));

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(userDO.getUserId());
        userInfo.setDepartment(department);
        userInfo.setName(userDO.getName());
        userInfo.setTel(userDO.getTel());
        userInfo.setEmail(userDO.getEmail());
        userInfo.setRole(role);

        return userInfo;
    }
}
