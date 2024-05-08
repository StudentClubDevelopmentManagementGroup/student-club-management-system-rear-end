package team.project.module.user.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.user.internal.mapper.UserMapper;
import team.project.module.user.internal.model.entity.UserDO;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoIServiceImpl implements UserInfoServiceI {

    @Autowired
    UserMapper userMapper;

    /**
     * 详见：{@link UserInfoServiceI#selectUserInfo}
     * */
    @Override
    public UserInfoDTO selectUserInfo(String userId) {
        UserDO userInfo = userMapper.selectUserInfo(userId);
        if (userInfo == null) {
            return null;
        }

        UserInfoDTO result = new UserInfoDTO();
        result.setUserId(userInfo.getUserId());
        result.setDepartmentId(userInfo.getDepartmentId());
        result.setName(userInfo.getName());
        result.setTel(userInfo.getTel());
        result.setEmail(userInfo.getEmail());
        result.setRole(userInfo.getRole());

        return result;
    }

    /**
     * 详见：{@link UserInfoServiceI#selectUserBasicInfo}
     * */
    @Override
    public UserBasicInfoDTO selectUserBasicInfo(String userId) {
        UserDO userBasicInfo = userMapper.selectBasicInfo(userId);
        if (userBasicInfo == null) {
            return null;
        }

        UserBasicInfoDTO result = new UserBasicInfoDTO();
        result.setUserId(userId);
        result.setName(userBasicInfo.getName());
        result.setRole(userBasicInfo.getRole());

        return result;
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

            UserBasicInfoDTO userBasicInfoDTO = new UserBasicInfoDTO();
            userBasicInfoDTO.setUserId(userDO.getUserId());
            userBasicInfoDTO.setName(userDO.getName());
            userBasicInfoDTO.setRole(userDO.getRole());

            result.add(userBasicInfoDTO);
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
