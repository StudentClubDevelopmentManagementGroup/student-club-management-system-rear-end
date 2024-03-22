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
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        String serverPort = ctx.getEnvironment().getProperty("server.port");
        Logger logger = LoggerFactory.getLogger("[临时测试用]" + Application.class);

        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "127.0.0.1";
        }

        logger.info(
            """
            api 说明文档：{}/swagger-ui/index.html
            文件存储测试：{}/html/test/file-storage.html
            """.replace("{}", "http://" + hostAddress + ":" + serverPort)
        );
    }
}
