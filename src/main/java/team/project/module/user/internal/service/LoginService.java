package team.project.module.user.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;

@Service
public class LoginService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TblUserMapper userMapper;

    @Autowired
    ModelConverter modelConverter;

    public UserInfoVO login(String userId, String password) {
        TblUserDO user = userMapper.selectOne(userId, password);
        if (user == null) {
            throw new ServiceException(ServiceStatus.UNAUTHORIZED, "账号不存在或密码错误");
        }

        return modelConverter.toUserInfoVO(user);
    }
}
