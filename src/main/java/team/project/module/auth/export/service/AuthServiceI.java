package team.project.module.auth.export.service;

import team.project.module.auth.export.exception.AuthenticationFailureException;

public interface AuthServiceI {

    /**
     * <p> 验证用户身份，要求用户在指定社团担任负责人（或者是超级管理员）
     * <p> 如果用户身份不满足要求，则抛出 {@link AuthenticationFailureException}
     * @param userId 用户的学号/工号
     * @param clubId 社团编号
     * @param message 用户身份不符合要求时，所抛出异常携带的提示信息
     * */
    void requireClubManager(String userId, long clubId, String message);

    /**
     * <p> 验证用户身份，要求用户在指定社团的成员（或者是超级管理员）
     * <p> 如果用户身份不满足要求，则抛出 {@link AuthenticationFailureException}
     * @param userId 用户的学号/工号
     * @param clubId 社团编号
     * @param message 用户身份不符合要求时，所抛出异常携带的提示信息
     * */
    void requireClubMember(String userId, long clubId, String message);
}
