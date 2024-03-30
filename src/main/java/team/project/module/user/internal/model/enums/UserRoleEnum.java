package team.project.module.user.internal.model.enums;

import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;

@AllArgsConstructor
public enum UserRoleEnum {
    STUDENT     (0b00001), /* 学生 */
    TEACHER     (0b00010), /* 教师 */
    CLUB_MANAGER(0b00100), /* 社团负责人 */
    SUPER_ADMIN (0b01000), /* 超级管理员 */
    ;

    public final int    value;

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
    public static boolean hasRole(int userRoleCode, UserRoleEnum role) {
        return switch (role) {
            case STUDENT      -> 0 != (userRoleCode & UserRoleEnum.STUDENT.value);
            case TEACHER      -> 0 != (userRoleCode & UserRoleEnum.TEACHER.value);
            case CLUB_MANAGER -> 0 != (userRoleCode & UserRoleEnum.CLUB_MANAGER.value);
            case SUPER_ADMIN  -> 0 != (userRoleCode & UserRoleEnum.SUPER_ADMIN.value);
        };
    }

    /**
     * 给当前用户的角色码添加指定角色
     * @param currRoleCode 当前的角色码
     * @param toAddRole 要添加的角色
     * @return 添加角色后的新角色码（注意记得对原来旧的角色码做重新赋值）
     * */
    public static int addRole(int currRoleCode, UserRoleEnum toAddRole) {
        Assert.isTrue(
           ! (   (hasRole(currRoleCode, UserRoleEnum.TEACHER) && toAddRole == UserRoleEnum.STUDENT)
              || (hasRole(currRoleCode, UserRoleEnum.STUDENT) && toAddRole == UserRoleEnum.TEACHER))
            ,"学生和教师的角色是互斥的，不能同时拥有");

        int newRoleCode = currRoleCode;

        switch (toAddRole) {
            case STUDENT      -> newRoleCode |= UserRoleEnum.STUDENT.value;
            case TEACHER      -> newRoleCode |= UserRoleEnum.TEACHER.value;
            case CLUB_MANAGER -> newRoleCode |= UserRoleEnum.CLUB_MANAGER.value;
            case SUPER_ADMIN  -> newRoleCode |= UserRoleEnum.SUPER_ADMIN.value;
        }

        return newRoleCode;
    }

    /**
     * 给当前用户的角色码移除指定角色
     * @param currRoleCode 当前的角色码
     * @param toRemoveRole 要移除的角色
     * @return 移除角色后的新角色码（记得对原来旧的角色码做重新赋值）
     * */
    public static int removeRole(int currRoleCode, UserRoleEnum toRemoveRole) {

        Assert.isTrue((toRemoveRole != UserRoleEnum.STUDENT && toRemoveRole != UserRoleEnum.TEACHER)
            , "学生和教师角色无法移除");

        int newRoleCode = currRoleCode;

        switch (toRemoveRole) {
            case CLUB_MANAGER -> newRoleCode &= ~UserRoleEnum.CLUB_MANAGER.value;
            case SUPER_ADMIN  -> newRoleCode &= ~UserRoleEnum.SUPER_ADMIN.value;
        };

        return newRoleCode;
    }
}
