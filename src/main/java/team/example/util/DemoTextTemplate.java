package team.example.util;

import team.project.util.texttmpl.TextTemplate;

import java.util.Map;

public class DemoTextTemplate {

    public static void main(String[] args) {
        /* 模板文本 */
        String tmplText = """
            <div>
                <h1>天气报告</h1>
                <p>日期: <!--{ date }--></p>
                <p>时间: <!--{ time }--></p>
                <p>天气: <!--{ weather }--></p>
                <p>气温: <!--{ temperature }--></p>
            </div>
            """;

        /* 构造模板，指定参数分界符是 <!--{  }--> */
        TextTemplate tmpl = new TextTemplate(tmplText, "<!--{", "}-->");

        /* 查看模板中的占位参数 */
        System.out.println("param list = " + tmpl.getParams());

        System.out.println("---------");

        /* 以键值对形式提供参数 */
        Map<String, Object> params = Map.of(
            "date", "2024-05-21",
            /* "time", "00:00", */
            "weather", "晴天",
            "temperature", "25°C"
        );
        String renderedText1 = tmpl.render(params);
        System.out.println(renderedText1);

        System.out.println("---------");

        /* 以列表形式向模板传递参数 */
        String renderedText2 = tmpl.render("2024-05-21", "00:00", "阵雨" /*, "14C" */);
        System.out.println(renderedText2);
    }
}
