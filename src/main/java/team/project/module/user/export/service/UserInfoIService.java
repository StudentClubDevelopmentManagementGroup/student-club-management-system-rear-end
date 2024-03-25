package team.project.module.user.export.service;

import team.project.module.user.export.model.datatransfer.UserInfoDTO;

public interface UserInfoIService {

    /**
     * 根据学号/工号获取用户信息
     */
    UserInfoDTO getUserInfoByUserId(String userId);

    /**
     * 给用户增添“社团负责人”身份
     * */
    void addClubManagerRoleToUser(String userId);
}
