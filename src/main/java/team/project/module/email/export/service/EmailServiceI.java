package team.project.module.email.export.service;

import team.project.module.email.export.model.query.SendEmailQO;

public interface EmailServiceI {

    /**
     * 发送邮件
     * @param sendEmailQO 详见 {@link SendEmailQO}
     * @return 如果发送成功返回 true，如果发送失败（发送时出现异常）则返回 false
     * */
    boolean SendEmail(SendEmailQO sendEmailQO);
}
