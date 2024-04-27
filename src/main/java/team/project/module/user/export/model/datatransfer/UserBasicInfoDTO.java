package team.project.module.user.export.model.datatransfer;

import lombok.Data;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.internal.model.enums.UserRoleEnum;

@Data
public class UserBasicInfoDTO {
    String  userId;
    String  name;
    Integer role;

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(UserRole role) {
        return UserRole.hasRole(this.role, role);
    }
}
