package team.project.module.user.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.internal.mapper.UserMapper;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;

@Service
public class LoginService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ModelConverter modelConverter;

    public UserInfoVO login(String userId, String password) {
        UserDO user = userMapper.selectUserInfo(userId, password);
        return user == null ? null : modelConverter.toUserInfoVO(user);
    }
}
