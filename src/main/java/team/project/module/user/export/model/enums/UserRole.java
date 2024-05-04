package team.project.module.user.export.model.enums;

import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

@AllArgsConstructor
public enum UserRole {
    STUDENT     (0b000001), /* 学生 */
    TEACHER     (0b000010), /* 教师 */
    CLUB_MEMBER (0b000100), /* 社团成员 */
    CLUB_MANAGER(0b001000), /* 社团负责人 */
    SUPER_ADMIN (0b010000), /* 超级管理员 */
    ;

    private final int value;

    /**
     * 获取空的角色码，没有任何角色
     * */
    public static int getEmptyRoleCode() {
        return 0;
    }

    /**
     * 通过用户的角色码，判断该角色码是否拥有指定角色
     * @param userRoleCode 当前用户的角色码
     * @param role 要判断的指定角色
     * @return 如果该角色码拥有指定角色，则返回 true，否则返回 false
     * */
    public static boolean hasRole(int userRoleCode, UserRole role) {
        return switch (role) {
            case STUDENT      -> 0 != (userRoleCode & UserRole.STUDENT.value);
            case TEACHER      -> 0 != (userRoleCode & UserRole.TEACHER.value);
            case CLUB_MEMBER  -> 0 != (userRoleCode & UserRole.CLUB_MEMBER.value);
            case CLUB_MANAGER -> 0 != (userRoleCode & UserRole.CLUB_MANAGER.value);
            case SUPER_ADMIN  -> 0 != (userRoleCode & UserRole.SUPER_ADMIN.value);
        };
    }

    /**
     * 给当前用户的角色码添加指定角色
     * @param currRoleCode 当前的角色码
     * @param toAddRole 要添加的角色
     * @return 添加角色后的新角色码（注意记得对原来旧的角色码做重新赋值）
     * */
    public static int addRole(int currRoleCode, UserRole toAddRole) {
        Assert.isTrue(
           ! (   (hasRole(currRoleCode, UserRole.TEACHER) && toAddRole == UserRole.STUDENT)
              || (hasRole(currRoleCode, UserRole.STUDENT) && toAddRole == UserRole.TEACHER))
            , "学生和教师的角色是互斥的，不能同时拥有");

        int newRoleCode = currRoleCode;

        switch (toAddRole) {
            case STUDENT      -> newRoleCode |= UserRole.STUDENT.value;
            case TEACHER      -> newRoleCode |= UserRole.TEACHER.value;
            case CLUB_MEMBER  -> newRoleCode |= UserRole.CLUB_MEMBER.value;
            case CLUB_MANAGER -> newRoleCode |= UserRole.CLUB_MANAGER.value;
            case SUPER_ADMIN  -> newRoleCode |= UserRole.SUPER_ADMIN.value;
            default -> { assert false; }
        }

        return newRoleCode;
    }

    /**
     * 给当前用户的角色码移除指定角色
     * @param currRoleCode 当前的角色码
     * @param toRemoveRole 要移除的角色
     * @return 移除角色后的新角色码（记得对原来旧的角色码做重新赋值）
     * */
    public static int removeRole(int currRoleCode, UserRole toRemoveRole) {
        Assert.isTrue((toRemoveRole != UserRole.STUDENT && toRemoveRole != UserRole.TEACHER)
            , "学生和教师角色无法移除");

        int newRoleCode = currRoleCode;

        switch (toRemoveRole) {
            case CLUB_MEMBER  -> newRoleCode &= ~UserRole.CLUB_MEMBER.value;
            case CLUB_MANAGER -> newRoleCode &= ~UserRole.CLUB_MANAGER.value;
            case SUPER_ADMIN  -> newRoleCode &= ~UserRole.SUPER_ADMIN.value;
            default -> { assert false; }
        };

        return newRoleCode;
    }
}
