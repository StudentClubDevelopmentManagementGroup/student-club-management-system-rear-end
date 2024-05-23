package team.project.module.user.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.user.internal.dao.UserDAO;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.util.ModelConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoIServiceImpl implements UserInfoServiceI {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ModelConverter modelConverter;

    /**
     * 详见：{@link UserInfoServiceI#selectUserInfo}
     * */
    @Override
    public UserInfoDTO selectUserInfo(String userId) {
        assert userId != null;

        UserDO userInfo = userDAO.selectUserInfo(userId);
        return modelConverter.toUserInfoDTO(userInfo);
    }

    /**
     * 详见：{@link UserInfoServiceI#selectUserBasicInfo}
     * */
    @Override
    public UserBasicInfoDTO selectUserBasicInfo(String userId) {
        assert userId != null;

        UserDO userBasicInfo = userDAO.selectUserBasicInfo(userId);
        return modelConverter.toUserBasicInfoDTO(userBasicInfo);
    }

    /**
     * 详见：{@link UserInfoServiceI#getUserName}
     */
    @Override
    public String getUserName(String userId) {
        assert userId != null;

        UserDO userBasicInfo = userDAO.selectUserBasicInfo(userId);
        return userBasicInfo == null ? null : userBasicInfo.getName();
    }

    /**
     * 详见：{@link UserInfoServiceI#searchUser}
     */
    @Override
    public List<UserBasicInfoDTO> searchUser(String userName) {
        assert userName != null;

        List<UserBasicInfoDTO> result = new ArrayList<>();
        for (UserDO userBasicInfo : userDAO.searchUserBasicInfo(userName)) {
            result.add( modelConverter.toUserBasicInfoDTO(userBasicInfo) );
        }
        return result;
    }

    /**
     * 详见：{@link UserInfoServiceI#selectUserRole}
     * */
    @Override
    public Integer selectUserRole(String userId) {
        assert userId != null;

        return userDAO.selectRoleCode(userId);
    }

    /**
     * 详见：{@link UserInfoServiceI#addRoleToUser}
     * */
    @Override
    public int addRoleToUser(String userId, UserRole roleToAdd) {
        assert userId    != null;
        assert roleToAdd != null;

        return userDAO.addRoleToUser(userId, roleToAdd);
    }

    /**
     * 详见：{@link UserInfoServiceI#removeRoleFromUser}
     * */
    @Override
    public int removeRoleFromUser(String userId, UserRole roleToRemove) {
        assert userId       != null;
        assert roleToRemove != null;

        return userDAO.removeRoleFromUser(userId, roleToRemove);
    }
}
