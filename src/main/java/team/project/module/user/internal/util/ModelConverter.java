package team.project.module.user.internal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.tmp.service.TmpDepartmentService;

@Component("user-util-ModelConverter")
public class ModelConverter {

    @Autowired
    TmpDepartmentService departmentService; /* ljh_TODO：取消对 tmp 模块的依赖 */

    public UserInfoVO toUserInfoVO(UserDO userDO) {
        if (null == userDO)
            return null;

        UserInfoVO.UserRoleInfo role = new UserInfoVO.UserRoleInfo();
        role.setStudent(userDO.hasRole(UserRole.STUDENT));
        role.setTeacher(userDO.hasRole(UserRole.TEACHER));
        role.setClubMember(userDO.hasRole(UserRole.CLUB_MEMBER));
        role.setClubManager(userDO.hasRole(UserRole.CLUB_MANAGER));
        role.setSuperAdmin(userDO.hasRole(UserRole.SUPER_ADMIN));

        UserInfoVO.DepartmentInfo department = new UserInfoVO.DepartmentInfo();
        department.setDepartmentId(userDO.getDepartmentId());
        department.setDepartmentName(departmentService.getNameById(userDO.getDepartmentId()));

        UserInfoVO result = new UserInfoVO();
        result.setUserId(userDO.getUserId());
        result.setDepartment(department);
        result.setName(userDO.getName());
        result.setTel(userDO.getTel());
        result.setEmail(userDO.getEmail());
        result.setRole(role);

        return result;
    }

    public UserInfoDTO toUserInfoDTO(UserDO userDO) {
        if (null == userDO)
            return null;

        UserInfoDTO result = new UserInfoDTO();
        result.setUserId(userDO.getUserId());
        result.setDepartmentId(userDO.getDepartmentId());
        result.setName(userDO.getName());
        result.setTel(userDO.getTel());
        result.setEmail(userDO.getEmail());
        result.setRole(userDO.getRole());

        return result;
    }

    public UserBasicInfoDTO toUserBasicInfoDTO(UserDO userDO) {
        if (null == userDO)
            return null;

        UserBasicInfoDTO result = new UserBasicInfoDTO();
        result.setUserId(userDO.getUserId());
        result.setName(userDO.getName());
        result.setRole(userDO.getRole());

        return result;
    }
}
