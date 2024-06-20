package team.project.module.user.internal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.internal.dao.UserDAO;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.internal.util.Util;
import team.project.module.util.email.export.model.query.SendEmailQO;
import team.project.module.util.email.export.service.EmailServiceI;
import team.project.module.util.email.export.util.EmailUtil;
import team.project.util.expiringmap.ExpiringMap;
import team.project.util.texttmpl.TextTemplate;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RegisterService {

    @Autowired
    EmailServiceI emailService;

    @Autowired
    UserDAO userDAO;

    /* 存储验证码（暂时将验证码存储在内存而不是数据库），验证码的有效时长 5 分钟 */
    private final ExpiringMap<String, String> registerCodes = new ExpiringMap<>(TimeUnit.MINUTES.toMillis(5));

    /* 邮件内容，使用 html 模板 */
    private final TextTemplate sendCodeTmpl = new TextTemplate(
        EmailUtil.formatAndWrapCSS("""
        <h1> GUET 社团管理系统 注册账号 </h1>
        <p>  验证码<em> <!--{{ code }}--> </em></p>
        <p>  该验证码 5 分钟内有效，请勿泄漏于他人 </p>
        """), "<!--{{", "}}-->"
    );

    public void sendCodeEmail(String email) {
        if (registerCodes.getRemainingTime(email) > TimeUnit.MINUTES.toMillis(4))
            throw new ServiceException(ServiceStatus.TOO_MANY_REQUESTS, "发送验证码过于频繁");

        String code = Util.randomVerificationCode(6);

        SendEmailQO sendEmailQO = new SendEmailQO();
        sendEmailQO.setSendTo(email);
        sendEmailQO.setSubject("【GUET 社团管理系统】注册账号");
        sendEmailQO.setContent(sendCodeTmpl.render(code));
        sendEmailQO.setHtml(true);

        if ( ! emailService.sendEmail(sendEmailQO)) {
            log.error("邮件发送失败");
            /* registerCodes.remove(email); */
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "邮件发送失败");
        }

        registerCodes.put(email, code);
    }

    public void register(RegisterReq req) {

        /* 2024-06-20 ljh
           数据表对“用户所属院系”字段设有“外键”约束，对“学号/工号”字段设有“唯一键”约束
        */

        int role = UserRole.getEmptyRoleCode();
        switch (req.getRole()) {
            case "student" -> UserRole.addRole(role, UserRole.STUDENT);
            case "teacher" -> UserRole.addRole(role, UserRole.TEACHER);
            default -> throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "不支持用户创建这个角色的账户");
        }

        if ( null != req.getRegisterCode()) { /* <- tmp */
            String storedCode = registerCodes.get(req.getEmail());
            if (storedCode == null || ! storedCode.equals(req.getRegisterCode()))
                throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "验证码错误或已过期");

            registerCodes.remove(req.getEmail());
        }

        UserDO user = new UserDO();
        user.setUserId(req.getUserId());
        user.setDepartmentId(req.getDepartmentId());
        user.setPassword(req.getPassword()); /* <- ljh_TODO: 待加密 */
        user.setName(req.getName());
        user.setTel(req.getTel());
        user.setEmail(req.getEmail());
        user.setRole(role);

        try {
            userDAO.insert(user);
        }
        catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                /* 2024-03-31 依目前的表设计，只有”学号/工号已存在“会触发”唯一键“异常 */
                log.info("用户注册失败：（可能是因为学号/工号已存在？）", e);
            }
            else if (e instanceof DataIntegrityViolationException) {
                /* 2024-04-10 依目前的表设计，只有”院系id不存在“会触发”外键“异常 */
                log.info("用户注册失败：（可能是因为外键院系id不存在？）", e);
            }
            else {
                log.error("用户注册失败：", e);
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "注册失败");
            }
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "注册失败");
        }
    }

    public void unregister(String userId, String password) {
        int result = userDAO.logicalDelete(userId, password);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.UNAUTHORIZED, "账号不存在或密码错误");
        }
    }
}
