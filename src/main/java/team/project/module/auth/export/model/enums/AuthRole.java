package team.project.module.auth.export.model.enums;

public enum AuthRole {
    ;
    public static final String STUDENT      = "STUDENT";
    public static final String TEACHER      = "TEACHER";
    public static final String CLUB_MANAGER = "CLUB_MANAGER";
    public static final String SUPER_ADMIN  = "SUPER_ADMIN";

    /* 使用 @SaCheckRole 注解鉴权时，不要直接用字符串写角色，而是用本枚举类封装的常量 */
}
