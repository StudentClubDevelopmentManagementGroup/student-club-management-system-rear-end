package team.project.module.user.internal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;

/**
 * <p>  本类负责构建邮件的内容
 * <p>  构建过程如下：
 * <ol>
 * <li> 读取资源文件夹下的 html 模板
 * <li> 如果读取失败，则使用在代码中硬编码的邮件内容模板
 * <li> 用传入的参数替换模板中的占位符，得到邮件内容
 * </ol>
 * */
public class EmailContentBuilder {
    private static final Logger log = LoggerFactory.getLogger(EmailContentBuilder.class);

    /**
     * 发送登录验证码
     * */
    public static String SendLoginCode(String code) {
        return fillTmpl(SEND_LOGIN_CODE_HTML_TMPL, code, "5");
    }

    /* --------- */

    /**
     * 读取资源文件夹下的 html 模板
     * @param filepath html 模板文件的路径（以资源文件夹 /resource 为基）
     * @return 如果读取成功则返回 html 模板的字符串量，否则返回 null
     * */
    private static String readTmpl(String filepath) {
        Resource resource = new ClassPathResource(filepath);
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = fileReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } catch (Exception e) {
            log.error("读取 html 模板时出现异常", e);
            return null;
        }
    }

    /**
     * 用传入的参数替换模板中的占位符
     * @param htmlTmpl html 模板
     * @param args 替换参数
     * */
    private static String fillTmpl(String htmlTmpl, Object ...args) {
         return MessageFormat.format(htmlTmpl, args);
    }

    /* 发送登录验证码的 html 模板 */
    private static final String SEND_LOGIN_CODE_HTML_TMPL;

    /* 读取模板内容 */
    static {
        String htmlTmpl = readTmpl("static/tmpl/user/send-login-code.html");
        SEND_LOGIN_CODE_HTML_TMPL = htmlTmpl != null ? htmlTmpl : """
            <h1>GUET 社团管理系统</h1>
            <p>您正在进行邮箱登录，验证码<em> {0} </em></p>
            <p>该验证码 {1} 分钟内有效，请勿泄漏于他人</p>
            """;
    }
}
