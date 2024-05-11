package team.project.module.user.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.user.internal.mapper.UserMapper;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.util.ModelConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoIServiceImpl implements UserInfoServiceI {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ModelConverter modelConverter;

    /**
     * 详见：{@link UserInfoServiceI#selectUserInfo}
     * */
    @Override
    public UserInfoDTO selectUserInfo(String userId) {
        UserDO userDO = userMapper.selectUserInfo(userId);
        if (userDO == null) {
            return null;
        } else {
            return modelConverter.toUserInfoDTO(userDO);
        }
    }

    /**
     * 详见：{@link UserInfoServiceI#selectUserBasicInfo}
     * */
    @Override
    public UserBasicInfoDTO selectUserBasicInfo(String userId) {
        UserDO userDO = userMapper.selectBasicInfo(userId);
        if (userDO == null) {
            return null;
        } else {
            return modelConverter.toUserBasicInfoDTO(userDO);
        }
    }

    /**
     * 详见：{@link UserInfoServiceI#getUserName}
     */
    @Override
    public String getUserName(String userId) {
        UserDO userBasicInfo = userMapper.selectBasicInfo(userId);
        return userBasicInfo == null ? null : userBasicInfo.getName();
    }

    /**
     * 详见：{@link UserInfoServiceI#searchUser}
     */
    @Override
    public List<UserBasicInfoDTO> searchUser(String userName) {
        List<UserBasicInfoDTO> result = new ArrayList<>();
        for (UserDO userDO : userMapper.searchUserBasicInfo(userName)) {
            result.add(modelConverter.toUserBasicInfoDTO(userDO) );
        }
        return result;
    }

    /**
     * 详见：{@link UserInfoServiceI#selectUserRole}
     * */
    @Override
    public Integer selectUserRole(String userId) {
        return userMapper.selectRoleCode(userId);
    }

    /**
     * 详见：{@link UserInfoServiceI#addRoleToUser}
     * */
    @Override
    public int addRoleToUser(String userId, UserRole roleToAdd) {
        return userMapper.addRoleToUser(userId, roleToAdd);
    }

    /**
     * 详见：{@link UserInfoServiceI#removeRoleFromUser}
     * */
    @Override
    public int removeRoleFromUser(String userId, UserRole roleToRemove) {
        return userMapper.removeRoleFromUser(userId, roleToRemove);
    }
}
