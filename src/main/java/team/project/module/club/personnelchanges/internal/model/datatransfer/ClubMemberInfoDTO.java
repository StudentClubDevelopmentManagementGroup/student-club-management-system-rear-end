package team.project.module.club.personnelchanges.internal.model.datatransfer;

import lombok.Data;
import team.project.module.user.export.model.enums.UserRole;

@Data
public class ClubMemberInfoDTO {
    String  userId;
    Long    departmentId;
    String  name;
    String  tel;
    String  email;
    Integer role;
    Integer ucrole;

    /* 判断用户是否拥有指定角色（user表的角色） */
    public boolean hasRole(UserRole role) {
        return UserRole.hasRole(this.role, role);
    }

    /* 判断该成员在社团中的角色 */
    public boolean isMember() {
        return (this.ucrole & 1) != 0;
    }

    /* 判断该成员在社团中的角色 */
    public boolean isManager() {
        return (this.ucrole & 2) != 0;
    }
}
