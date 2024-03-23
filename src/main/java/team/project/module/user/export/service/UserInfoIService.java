package team.project.module.user.export.service;

import team.project.module.user.export.model.datatransfer.UserInfoDTO;

public interface UserInfoIService {

    /**
     * 根据学号/工号获取用户信息
     */
    UserInfoDTO getUserInfoByUserId(String userId);
}
