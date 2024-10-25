package team.project.module.user.export.service;

import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;

import java.util.List;

public interface UserInfoServiceI {

    /* -- 查询用户信息 -- */

    /**
     * 从 tbl_user 表中查询指定用户的信息
     * @param userId 学号/工号
     * @return 用户账号信息（查询不到则返回 null）
     */
    UserInfoDTO selectUserInfo(String userId);

    /**
     * 从 tbl_user 表中查询指定用户的基本信息
     * @param userId 学号/工号
     * @return 用户基本信息（查询不到则返回 null）
     */
    UserBasicInfoDTO selectUserBasicInfo(String userId);

    /**
     * 从 tbl_user 表中查询指定用户的姓名
     * @param userId 学号/工号
     * @return 用户姓名（查询不到则返回 null）
     */
    String getUserName(String userId);

    /**
     * <p> 从 tbl_user 表中搜索相关用户的基本信息（模糊查询）
     * <p style="color: yellow;">
     *     注意：
     *     注意：如果 userName 是 null，或是空字符串，或是只含有空白字符的字符串，
     *     则不会执行 SQL 查询操作，而是直接返回空列表
     * </p>
     * @param userName 用户的姓名（查询关键字）
     * @return 搜索到的用户基本信息（如果搜索不出结果则返回空列表，不会返回 null）
     * */
    List<UserBasicInfoDTO> searchUser(String userName);

    /* -- 查询、更新用户角色 -- */

    /**
     * <p> 从 tbl_user 表中查询指定用户的角色码
     * <p> 用 {@link UserRole#hasRole(int roleCode, UserRole role)} 来判断用户是否拥有指定角色
     * @param userId 学号/工号
     * @return 该用户的角色码（roleCode），如果查询不到用户否则返回 null
     * */
    Integer selectUserRole(String userId);

    /**
     * 更新 tbl_user 表，给指定用户添加角色（不需要考虑用户当前是否拥有这个角色）
     * @param userId 学号/工号
     * @param roleToAdd 要添加的角色
     * @return 数据库 tbl_user 表受 update 语句影响的行数（如果增添成功理应返回 1，否则返回 0）
     * */
    int addRoleToUser(String userId, UserRole roleToAdd);

    /**
     * 更新 tbl_user 表，给指定用户移除角色（不需要考虑用户当前是否拥有这个角色）
     * @param userId 学号/工号
     * @param roleToRemove 要移除的角色
     * @return 数据库 tbl_user 表受 update 语句影响的行数（如果移除成功理应返回 1，否则返回 0）
     * */
    int removeRoleFromUser(String userId, UserRole roleToRemove);
}
