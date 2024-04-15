package team.project.module.user.export.model.enums;

import lombok.AllArgsConstructor;
import team.project.module.user.internal.model.enums.UserRoleEnum;

@AllArgsConstructor
public enum UserRole {
    STUDENT     (UserRoleEnum.STUDENT),
    TEACHER     (UserRoleEnum.TEACHER),
    CLUB_MEMBER (UserRoleEnum.CLUB_MEMBER),
    CLUB_MANAGER(UserRoleEnum.CLUB_MANAGER),
    SUPER_ADMIN (UserRoleEnum.SUPER_ADMIN),
    ;

    /* private ! */ public final UserRoleEnum r; /* <- 当前枚举类，是这个枚举类的封装 */
}
