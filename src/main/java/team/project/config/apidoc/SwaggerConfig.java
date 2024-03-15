package team.project.config.apidoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* Swagger 用来自动生成 API 文档的配置类 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiIntro() {
        Info info = new Info()
                .title("GUET 学生-社团-管理系统 API 说明文档");
        return new OpenAPI().info(info);
    }
}
