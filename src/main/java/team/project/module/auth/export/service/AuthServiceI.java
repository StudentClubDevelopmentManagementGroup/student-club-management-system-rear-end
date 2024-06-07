package team.project.module.auth.export.service;

import team.project.module.auth.export.exception.AuthenticationFailureException;

public interface AuthServiceI {

    /**
     * <p> 验证用户身份，要求用户至少拥有下述角色中的一个：
     * <p> 指定社团的成员 | 超级管理员
     *
     * @param userId 用户的学号/工号
     * @param clubId 社团编号
     * @param message 用户身份不符合要求时，所抛出异常携带的提示信息
     * @throws AuthenticationFailureException 如果用户身份不符合要求则抛出异常
     * */
    void requireClubMember(String userId, long clubId, String message);

    /**
     * <p> 验证用户身份，要求用户至少拥有下述角色中的一个：
     * <p> 指定社团的学生负责人 | 指定社团的教师负责人 | 超级管理员
     *
     * @param userId 用户的学号/工号
     * @param clubId 社团编号
     * @param message 用户身份不符合要求时，所抛出异常携带的提示信息
     * @throws AuthenticationFailureException 如果用户身份不符合要求则抛出异常
     * */
    void requireClubManager(String userId, long clubId, String message);

    /**
     * <p> 验证用户身份，要求用户至少拥有下述角色中的一个：
     * <p> 指定社团的教师负责人 | 超级管理员
     *
     * @param userId 用户的学号/工号
     * @param clubId 社团编号
     * @param message 用户身份不符合要求时，所抛出异常携带的提示信息
     * @throws AuthenticationFailureException 如果用户身份不符合要求则抛出异常
     * */
    void requireClubTeacherManager(String userId, long clubId, String message);

    /**
     * <p> 验证用户身份，要求用户必须是 超级管理员
     *
     * @param userId 用户的学号/工号
     * @param message 用户身份不符合要求时，所抛出异常携带的提示信息
     * @throws AuthenticationFailureException 如果用户身份不符合要求则抛出异常
     * */
    void requireSuperAdmin(String userId, String message);
}
