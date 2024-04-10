package team.project.module.auth.export.model.enums;

public enum AuthRole {
    ;
    public static final String STUDENT      = "STUDENT";
    public static final String TEACHER      = "TEACHER";
    public static final String CLUB_MEMBER  = "CLUB_MEMBER";
    public static final String CLUB_MANAGER = "CLUB_MANAGER";
    public static final String SUPER_ADMIN  = "SUPER_ADMIN";

    /* 使用 @SaCheckRole 注解鉴权时，不要直接用字符串写角色，而是用本枚举类封装的常量 */

    public static final String ROLE_NAME_STUDENT      = "学生";
    public static final String ROLE_NAME_TEACHER      = "教师";
    public static final String ROLE_NAME_CLUB_MEMBER  = "社团成员";
    public static final String ROLE_NAME_CLUB_MANAGER = "社团负责人";
    public static final String ROLE_NAME_SUPER_ADMIN  = "超级管理员";
}
