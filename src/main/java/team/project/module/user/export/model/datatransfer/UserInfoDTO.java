package team.project.module.user.export.model.datatransfer;

import lombok.Data;
import team.project.module.user.export.model.enums.Role;
import team.project.module.user.internal.model.enums.UserRole;

import java.util.ArrayList;

@Data
public class UserInfoDTO {
    String  userId;
    Long    departmentId;
    String  name;
    String  tel;
    String  email;
    Integer role;

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(Role role) {
        assert this.role != null : "当前角色码为 null";
        return UserRole.hasRole(this.role, role.r);
    }

    /* 获取用户的身份描述列表 */
    public ArrayList<String> getRoleList() {
        assert this.role != null : "当前角色码为 null";
        return UserRole.getExistingRoleDescriptions(this.role);
    }
}
