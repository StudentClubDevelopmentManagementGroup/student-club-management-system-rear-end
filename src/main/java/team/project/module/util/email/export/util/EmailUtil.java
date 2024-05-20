package team.project.module.util.email.export.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class EmailUtil {

    /**
     * <p>  本函数用于给篇幅较短的 html 格式的邮件内容添加简单的 css 样式：
     * <br> 将 html 模板中的占位符替换成对应的参数，并给替换后的 html 包裹一段 css
     * <p>  模板参数的替换规则同 {@link String#format(String, Object...) }，即使用 %s、%d 等作占位符
     * <br> css 样式来源于 {@link EmailUtil#CSS_FILEPATH} 文件（目前设定的样式不多，随项目进行会逐步增加）
     * <p>  如果模板篇幅较大，或需要自定义样式，则不建议使用本方法。建议使用其他的 html 模板引擎工具
     * @param htmlTmpl html 模板
     * @param args     html 模板内容中需要替换的参数
     * */
    public static String formatWithCSS(String htmlTmpl, Object ...args) {
        return "<div><style>" + CSS + "</style><div class='e'>" + String.format(htmlTmpl, args) + "</div></div>";
    }

    /* 从 .min.css 文件中读取 CSS 样式 */
    private static final String CSS_FILEPATH = "static/css/guet-email-reset.min.css";
    private static String CSS = "";
    static {
        try {
            CSS = Files.readString(Paths.get(new ClassPathResource(CSS_FILEPATH).getURI()));
        } catch (Exception e) {
            log.error("无法读取 css 文件：" + CSS_FILEPATH, e);
        }
    }
}
