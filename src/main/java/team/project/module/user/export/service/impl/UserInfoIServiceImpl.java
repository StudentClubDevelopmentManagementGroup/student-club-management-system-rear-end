package team.project.module.user.export.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoIService;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;

@Service
public class UserInfoIServiceImpl implements UserInfoIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TblUserMapper userMapper;

    public UserInfoDTO getUserInfoByUserId(String userId) {
        TblUserDO userInfo = userMapper.selectOne(userId);

        UserInfoDTO result = new UserInfoDTO();
        result.setUserId(userInfo.getUserId());
        result.setDepartmentId(userInfo.getDepartmentId());
        result.setName(userInfo.getName());
        result.setTel(userInfo.getTel());
        result.setEmail(userInfo.getEmail());
        result.setRole(userInfo.getRole());

        return result;
    }

    public int addRoleToUser(String userId, UserRole roleToAdd) {
        return userMapper.addRoleToUser(userId, roleToAdd.r);
    }

    public int removeRoleFromUser(String userId, UserRole roleToRemove) {
        return userMapper.removeRoleFromUser(userId, roleToRemove.r);
    }
}
