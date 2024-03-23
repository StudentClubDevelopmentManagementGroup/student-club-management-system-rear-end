package team.project.module.user.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;

@Service
public class UserInfoService {
    @Autowired
    TblUserMapper tblUserMapper;

    public TblUserDO getUserInfoByUserId(String userId) {
        return tblUserMapper.selectByUserId(userId);
    }
}
