package team.project.module.user.export.service;

import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.Role;

public interface UserIService {

    /**
     * 根据学号/工号获取用户信息
     */
    UserInfoDTO getUserInfoByUserId(String userId);

    /**
     * 给用户增添角色（不需要考虑用户当前是否拥有这个角色）
     * @return 数据库中受 update 语句影响的行数（如果增添成功返回 1，否则返回 0）
     * */
    int addRoleToUser(String userId, Role roleToAdd);

    /**
     * 移除用户角色（不需要考虑用户当前是否拥有这个角色）
     * @return 数据库中受 update 语句影响的行数（如果移除成功返回 1，否则返回 0）
     * */
    int removeRoleFromUser(String userId, Role roleToRemove);
}
