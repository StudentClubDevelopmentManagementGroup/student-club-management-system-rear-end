package team.project.module.user.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;

@Service
public class LoginService {

    @Autowired
    TblUserMapper userMapper;

    @Autowired
    ModelConverter modelConverter;

    public UserInfoVO login(String userId, String password) {
        TblUserDO user = userMapper.selectUserInfo(userId, password);
        return user == null ? null : modelConverter.toUserInfoVO(user);
    }
}
