package team.project.module.util.email.export.contentbuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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
@Component
public class EmailContentBuilder {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送登录验证码到邮箱
     * */
    public String SendLoginCode(String code) {

        String html = readHtmlTmpl("static/tmpl/email/send-login-code.html");

        if (html == null) { /* 读取不到 html 模板时，使用下方默认模板 */
            html = """
            <h1>GUET 社团管理系统</h1>
            <p>您正在进行邮箱登录，验证码 {0}</p>
            <p>切勿将验证码泄露于他人，本条验证码有效期 {1} 分钟</p>
            """;
        }

        return MessageFormat.format(html, code, "5");
    }

    /**
     * 读取资源文件夹下的 html 模板，读取失败则返回 null
     * */
    private String readHtmlTmpl(String filePath) {

        Resource resource = new ClassPathResource(filePath);
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
}
