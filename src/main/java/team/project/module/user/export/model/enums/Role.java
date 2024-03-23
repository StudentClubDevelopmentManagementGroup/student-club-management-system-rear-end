package team.project.module.user.export.model.enums;

import lombok.AllArgsConstructor;
import team.project.module.user.internal.model.enums.UserRole;

@AllArgsConstructor
public enum Role {
    STUDENT     (UserRole.STUDENT),     /* 学生 */
    TEACHER     (UserRole.TEACHER),     /* 教师 */
    CLUB_LEADER (UserRole.CLUB_LEADER), /* 社团负责人 */
    SUPER_ADMIN (UserRole.SUPER_ADMIN), /* 超级管理员 */
    ;

    public final UserRole r; /* <- 当前枚举类，是这个枚举类的封装 */
}
