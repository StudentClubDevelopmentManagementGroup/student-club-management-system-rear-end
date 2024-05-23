package team.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class Application {

    static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(Application.class, args);
        tmp();
    }

    private static void tmp() {
        Logger logger = LoggerFactory.getLogger("【临时测试用】");

        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        if ( ! assertionsEnabled)
            logger.warn("\033[31m【未开启断言！】在开发阶段，为确保代码的正确性和稳定性，请在 JVM 中启用断言");

        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
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
