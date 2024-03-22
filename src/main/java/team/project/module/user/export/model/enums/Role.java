package team.project.module.user.export.model.enums;

import lombok.AllArgsConstructor;
import team.project.module.user.internal.model.enums.UserRole;

@AllArgsConstructor
public enum Role {
    STUDENT     (UserRole.STUDENT),
    TEACHER     (UserRole.TEACHER),
    CLUB_LEADER (UserRole.CLUB_LEADER),
    SUPER_ADMIN (UserRole.SUPER_ADMIN),
    ;

    public final UserRole r; /* <- 当前枚举类，是这个枚举类的封装 */
}
