package team.project.module.user.internal.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.dao.UserDAO;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.request.UserIdAndCodeReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;
import team.project.module.user.internal.util.Util;
import team.project.module.util.email.export.model.query.SendEmailQO;
import team.project.module.util.email.export.service.EmailServiceI;
import team.project.module.util.email.export.util.EmailUtil;
import team.project.util.expiringmap.ExpiringMap;
import team.project.util.texttmpl.TextTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginService {
    @Autowired
    EmailServiceI emailService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ModelConverter modelConverter;

    /* -- 密码登录 -- */

    /**
     * 通过用户名和密码登录
     * @return 登录成功返回用户信息，登录失败返回 null
     * */
    public UserInfoVO login(String userId, String password) {
        String pwd = userDAO.selectPassword(userId);

        if ( ! Objects.equals(pwd, password))
            return null;

        return modelConverter.toUserInfoVO(userDAO.selectUserInfo(userId));
    }

    /* -- 邮箱登录 -- */

    /* 存储验证码（暂时将验证码存储在内存而不是数据库），验证码的有效时长 5 分钟 */
    private final ExpiringMap<String, String> loginCodes = new ExpiringMap<>(TimeUnit.MINUTES.toMillis(5));

    /* 邮件内容，使用 html 模板 */
    private final TextTemplate sendCodeTmpl = new TextTemplate(
        EmailUtil.formatAndWrapCSS("""
        <h1> GUET 社团管理系统 登录验证 </h1>
        <p>  您正在进行邮箱登录，验证码<em> <!--{{ code }}--> </em></p>
        <p>  该验证码 5 分钟内有效，请勿泄漏于他人 </p>
        """), "<!--{{", "}}-->"
    );

    /**
     * 通过用户名和邮箱验证码登录（发送验证码）
     * */
    public void sendCodeEmail(String userId) {

        if (loginCodes.getRemainingTime(userId) > TimeUnit.MINUTES.toMillis(4))
            throw new ServiceException(ServiceStatus.TOO_MANY_REQUESTS, "发送验证码过于频繁");

        String userEmail = userDAO.selectEmail(userId);
        if (userEmail == null) {
            return; /* 将“查询不到用户”视为发送成功 */
        }

        String code = Util.randomVerificationCode(8);

        SendEmailQO sendEmailQO = new SendEmailQO();
        sendEmailQO.setSendTo(userEmail);
        sendEmailQO.setSubject("【GUET 社团管理系统】登录验证");
        sendEmailQO.setContent(sendCodeTmpl.render(code));
        sendEmailQO.setHtml(true);

        if ( ! emailService.sendEmail(sendEmailQO)) {
            log.error("邮件发送失败");
            /* loginCodes.remove(userId); */
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "邮件发送失败");
        }

        loginCodes.put(userId, code);
    }

    /**
     * 通过用户名和邮箱验证码登录（登录）
     * @return 登录成功返回用户信息，登录失败返回 null
     * */
    public UserInfoVO login(UserIdAndCodeReq req) {
        String storedCode = loginCodes.get(req.getUserId());

        if (storedCode == null || ! storedCode.equals(req.getCode()))
            return null;

        loginCodes.remove(req.getUserId());

        UserDO userInfo = userDAO.selectUserInfo(req.getUserId());
        return modelConverter.toUserInfoVO(userInfo);
    }
}
