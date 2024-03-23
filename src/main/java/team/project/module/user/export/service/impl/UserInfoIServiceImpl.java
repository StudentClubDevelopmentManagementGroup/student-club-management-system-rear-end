package team.project.module.user.export.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.service.UserInfoService;

@Service
public class UserInfoIServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserInfoService userInfoService;

    /**
     * 通过学号/工号获取用户的信息
     * */
    public UserInfoDTO getUserInfoByUserId(String userId) {
        TblUserDO userInfo = userInfoService.getUserInfoByUserId(userId);

        UserInfoDTO result = new UserInfoDTO();
        result.setUserId(userInfo.getUserId());
        result.setDepartmentId(userInfo.getDepartmentId());
        result.setName(userInfo.getName());
        result.setTel(userInfo.getTel());
        result.setEmail(userInfo.getEmail());
        result.setRole(userInfo.getRole());

        return result;
    }

}
