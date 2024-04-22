package team.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

    static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(Application.class, args);
        tmp();
    }

    private static void tmp() {
        Logger logger = LoggerFactory.getLogger("【临时测试用】");

        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("获取本机地址失败", e);
            host = "127.0.0.1";
        }
        String port = ctx.getEnvironment().getProperty("server.port");

        logger.info("""
            api 说明文档：{}/swagger-ui/index.html
            api 说明文档：{}/html/test/swagger-with-login.html（ <- 快速登录 ）
            文件存储测试：{}/html/test/file-storage.html
            """.replace("{}", "http://" + host + ":" + port)
        );
    }
}
