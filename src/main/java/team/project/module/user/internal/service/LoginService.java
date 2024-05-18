package team.project.module.user.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.dao.UserDAO;
import team.project.module.user.internal.dao.VerificationDAO;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.request.UserIdAndCodeReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;
import team.project.module.util.email.export.model.query.SendEmailQO;
import team.project.module.util.email.export.service.EmailServiceI;

import java.util.UUID;

@Service
public class LoginService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EmailServiceI emailService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    VerificationDAO verificationDAO;

    @Autowired
    ModelConverter modelConverter;

    /**
     * 通过用户名和密码登录
     * @return 登录成功返回用户信息，登录失败返回 null
     * */
    public UserInfoVO login(String userId, String password) {
        UserDO user = userDAO.selectUserInfo(userId, password);
        return user == null ? null : modelConverter.toUserInfoVO(user);
    }

    /**
     * 通过用户名和邮箱验证码登录（发送验证码）
     * */
    public void sendCodeByEmail(String userId) {

        UserDO userInfo = userDAO.selectUserInfo(userId);
        if (userInfo == null) {
            return; /* 将“查询不到用户”视为发送成功 */
        }

        String code = UUID.randomUUID().toString().substring(0, 6);

        verificationDAO.put(userId, code);

        SendEmailQO sendEmailQO = new SendEmailQO();
        sendEmailQO.setSendTo(userInfo.getEmail());
        sendEmailQO.setSubject("GUET 社团管理系统");
        sendEmailQO.setContent("登录验证码：" + code);

        if ( ! emailService.SendEmail(sendEmailQO)) {
            log.error("邮件发送失败");
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "邮件发送失败");
        }
    }

    /**
     * 通过用户名和邮箱验证码登录（登录）
     * @return 登录成功返回用户信息，登录失败返回 null
     * */
    public UserInfoVO login(UserIdAndCodeReq req) {
        if ( ! verificationDAO.verify(req.getUserId(), req.getCode())) {
            return null;
        }

        UserDO user = userDAO.selectUserInfo(req.getUserId());
        return user == null ? null : modelConverter.toUserInfoVO(user);
    }
}
