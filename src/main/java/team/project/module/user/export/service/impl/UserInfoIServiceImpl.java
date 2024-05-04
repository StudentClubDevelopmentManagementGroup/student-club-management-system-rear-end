package team.project.module.user.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;

@Service
public class UserInfoIServiceImpl implements UserInfoServiceI {

    @Autowired
    TblUserMapper userMapper;

    @Override
    public UserInfoDTO selectUserInfo(String userId) {
        TblUserDO userInfo = userMapper.selectOne(userId);
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

    @Override
    public Integer selectUserRole(String userId) {
        return userMapper.selectRoleCode(userId);
    }

    @Override
    public boolean hasRole(String userId, UserRole role) {
        Integer roleCode = userMapper.selectRoleCode(userId);
        return roleCode != null && UserRole.hasRole(roleCode, role);
    }

    @Override
    public int addRoleToUser(String userId, UserRole roleToAdd) {
        return userMapper.addRoleToUser(userId, roleToAdd);
    }

    @Override
    public int removeRoleFromUser(String userId, UserRole roleToRemove) {
        return userMapper.removeRoleFromUser(userId, roleToRemove);
    }
}
