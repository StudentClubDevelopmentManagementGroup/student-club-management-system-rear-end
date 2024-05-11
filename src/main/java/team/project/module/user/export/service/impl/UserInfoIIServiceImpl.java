package team.project.module.user.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoIService;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoIIServiceImpl implements UserInfoIService {

    @Autowired
    TblUserMapper userMapper;

    /**
     * 详见：{@link UserInfoIService#selectUserInfo}
     * */
    @Override
    public UserInfoDTO selectUserInfo(String userId) {
        TblUserDO userInfo = userMapper.selectUserInfo(userId);
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
     * 详见：{@link UserInfoIService#selectUserBasicInfo}
     * */
    @Override
    public UserBasicInfoDTO selectUserBasicInfo(String userId) {
        TblUserDO userBasicInfo = userMapper.selectBasicInfo(userId);
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
     * 详见：{@link UserInfoIService#searchUsers}
     */
    @Override
    public List<UserBasicInfoDTO> searchUsers(String userName) {
        List<UserBasicInfoDTO> result = new ArrayList<>();

        for (TblUserDO userDO : userMapper.searchUsersBasicInfo(userName)) {

            UserBasicInfoDTO userBasicInfoDTO = new UserBasicInfoDTO();
            userBasicInfoDTO.setUserId(userDO.getUserId());
            userBasicInfoDTO.setName(userDO.getName());
            userBasicInfoDTO.setRole(userDO.getRole());

            result.add(userBasicInfoDTO);
        }

        return result;
    }

    /**
     * 详见：{@link UserInfoIService#selectUserRole}
     * */
    @Override
    public Integer selectUserRole(String userId) {
        return userMapper.selectRoleCode(userId);
    }

    /**
     * 详见：{@link UserInfoIService#addRoleToUser}
     * */
    @Override
    public int addRoleToUser(String userId, UserRole roleToAdd) {
        return userMapper.addRoleToUser(userId, roleToAdd);
    }

    /**
     * 详见：{@link UserInfoIService#removeRoleFromUser}
     * */
    @Override
    public int removeRoleFromUser(String userId, UserRole roleToRemove) {
        return userMapper.removeRoleFromUser(userId, roleToRemove);
    }
}
