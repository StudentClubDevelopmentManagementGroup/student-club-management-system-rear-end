package team.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import team.project.debug.NamingStyleChecker;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        ctx.getBean(NamingStyleChecker.class).checkNamingStyle();

        String serverPort = ctx.getEnvironment().getProperty("server.port");
        Logger logger = LoggerFactory.getLogger(Application.class);

        logger.info("api 说明文档：http://127.0.0.1:{}/swagger-ui/index.html", serverPort);
        logger.info("文件存储测试：http://127.0.0.1:{}/html/test/file-storage.html", serverPort);
    }
}
