package team.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        Logger logger = LoggerFactory.getLogger(Application.class);
        logger.info("api 说明文档：http://127.0.0.1:{}/swagger-ui/index.html",
                ctx.getEnvironment().getProperty("server.port")
        );
    }
}
