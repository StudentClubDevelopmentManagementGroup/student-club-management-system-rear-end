package team.project.module.util.email.export.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailQO {
    /**
     * 收件人的邮箱
     * */
    String sendTo;

    /**
     * 邮件的主题
     * */
    String subject;

    /**
     * 邮件的正文
     * */
    String content;

    /**
     * <p> 是否渲染 html 内容
     * <p> 如果是纯文本则设为 false，如果含有 html 元素则设为 true
     * */
    boolean isHtml = false;
}
