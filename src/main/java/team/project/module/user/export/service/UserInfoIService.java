package team.project.module.user.export.service;

import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;

public interface UserInfoIService {

    /**
     * 根据学号/工号获取用户信息
     */
    UserInfoDTO getUserInfoByUserId(String userId);

    /**
     * 更新 tbl_user 表，给指定用户添加角色（不需要考虑用户当前是否拥有这个角色）
     * @return 数据库 tbl_user 表受 update 语句影响的行数（如果增添成功返回 1，否则返回 0）
     * */
    int addRoleToUser(String userId, UserRole roleToAdd);

    /**
     * 更新 tbl_user 表，给指定用户移除角色（不需要考虑用户当前是否拥有这个角色）
     * @return 数据库 tbl_user 表受 update 语句影响的行数（如果移除成功返回 1，否则返回 0）
     * */
    int removeRoleFromUser(String userId, UserRole roleToRemove);
}
