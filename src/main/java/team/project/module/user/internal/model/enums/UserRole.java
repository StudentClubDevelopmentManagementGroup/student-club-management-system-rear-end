package team.project.module.user.internal.model.enums;

import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public enum UserRole {
    STUDENT     (0b00001, "学生"),
    TEACHER     (0b00010, "教师"),
    CLUB_MANAGER(0b00100, "社团负责人"),
    SUPER_ADMIN (0b01000, "超级管理员"),
    ;

    public final int    v; /* <- value */
    public final String roleName;

    /**
     * 获取空的角色码，没有任何角色
     * */
    public static int getEmptyRoleCode() {
        return 0;
    }

    /**
     * 通过用户的角色码，判断用户是否拥有指定角色
     * @param userRoleCode 当前用户的角色码
     * @param role 要判断的指定角色
     * @return 如果用户拥有指定角色，则返回 true，否则返回 false
     * */
    public static boolean hasRole(int userRoleCode, UserRole role) {
        return switch (role) {
            case STUDENT     -> 0 != (userRoleCode & UserRole.STUDENT.v);
            case TEACHER     -> 0 != (userRoleCode & UserRole.TEACHER.v);
            case CLUB_MANAGER -> 0 != (userRoleCode & UserRole.CLUB_MANAGER.v);
            case SUPER_ADMIN -> 0 != (userRoleCode & UserRole.SUPER_ADMIN.v);
        };
    }

    /**
     * 给当前用户的角色码添加指定角色
     * @param currRoleCode 当前的角色码
     * @param toAddRole 要添加的角色
     * @return 添加角色后的新角色码（注意记得对原来旧的角色码做重新赋值）
     * */
    public static int addRole(int currRoleCode, UserRole toAddRole) {

        assert   (toAddRole != UserRole.TEACHER && ! hasRole(currRoleCode, UserRole.STUDENT))
              || (toAddRole != UserRole.STUDENT && ! hasRole(currRoleCode, UserRole.TEACHER))
            : "学生和教师的角色是互斥的，不能同时拥有";

        int newRoleCode = currRoleCode;

        switch (toAddRole) {
            case STUDENT     -> newRoleCode |= UserRole.STUDENT.v;
            case TEACHER     -> newRoleCode |= UserRole.TEACHER.v;
            case CLUB_MANAGER -> newRoleCode |= UserRole.CLUB_MANAGER.v;
            case SUPER_ADMIN -> newRoleCode |= UserRole.SUPER_ADMIN.v;
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

        assert (toRemoveRole != UserRole.STUDENT && toRemoveRole != UserRole.TEACHER)
            : "学生和教师角色无法移除";

        int newRoleCode = currRoleCode;

        switch (toRemoveRole) {
            case CLUB_MANAGER -> newRoleCode &= ~UserRole.CLUB_MANAGER.v;
            case SUPER_ADMIN -> newRoleCode &= ~UserRole.SUPER_ADMIN.v;
        };

        return newRoleCode;
    }

    /* 获取已有角色描述列表 */
    public static ArrayList<String> getExistingRoleDescriptions(int currRoleCode) {

        ArrayList<String> roles = new ArrayList<>();

        if (hasRole(currRoleCode, UserRole.STUDENT))     roles.add(UserRole.STUDENT.roleName);
        if (hasRole(currRoleCode, UserRole.TEACHER))     roles.add(UserRole.TEACHER.roleName);
        if (hasRole(currRoleCode, UserRole.CLUB_MANAGER)) roles.add(UserRole.CLUB_MANAGER.roleName);
        if (hasRole(currRoleCode, UserRole.SUPER_ADMIN)) roles.add(UserRole.SUPER_ADMIN.roleName);

        return roles;
    }
}
