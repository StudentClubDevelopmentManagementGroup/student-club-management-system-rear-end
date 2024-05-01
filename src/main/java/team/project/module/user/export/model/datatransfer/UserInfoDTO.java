package team.project.module.user.export.model.datatransfer;

import lombok.Getter;
import lombok.Setter;
import team.project.module.user.export.model.enums.UserRole;

@Getter
@Setter
public class UserInfoDTO {
    String  userId;
    Long    departmentId;
    String  name;
    String  tel;
    String  email;
    Integer role;

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(UserRole role) {
        return UserRole.hasRole(this.role, role);
    }
}
