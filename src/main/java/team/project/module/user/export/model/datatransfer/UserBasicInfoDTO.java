package team.project.module.user.export.model.datatransfer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import team.project.module.user.export.model.enums.UserRole;

@Getter
@Setter
@Data
public class UserBasicInfoDTO {
    String  userId;
    String  name;
    Integer role; /* <- 标识用户身份的角色码 */

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(UserRole role) {
        return UserRole.hasRole(this.role, role);
    }
}
