package team.project.module.club.personnelchanges.internal.model.datatransfer;

import lombok.Data;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.internal.model.enums.UserRoleEnum;

@Data
public class ClubMemberInfoDTO {
    String  userId;
    Long    departmentId;
    String  name;
    String  tel;
    String  email;
    Integer role;
    Integer ucrole;

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(UserRole role) {
        return UserRoleEnum.hasRole(this.role, role.r);
    }

    public boolean isMember() {
        return (this.ucrole & 1)!=0;
    }
    public boolean isManager() {
        return (this.ucrole & 2)!=0;
    }
}
