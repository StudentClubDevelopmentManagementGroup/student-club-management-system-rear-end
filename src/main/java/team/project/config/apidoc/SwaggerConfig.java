package team.project.config.apidoc;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* Swagger 用来自动生成 API 文档的配置类 */
@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
            .group("所有 api")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    GroupedOpenApi user() {
        return GroupedOpenApi.builder()
            .group("用户（注册、登录、信息管理）")
            .packagesToScan("team.project.module.user")
            .build();
    }

    @Bean
    GroupedOpenApi clubManagement() {
        return GroupedOpenApi.builder()
            .group("社团-基地管理")
            .packagesToScan("team.project.module.club.management")
            .build();
    }

    @Bean
    GroupedOpenApi clubPersonalCharges() {
        return GroupedOpenApi.builder()
            .group("社团-成员变动")
            .packagesToScan("team.project.module.club.personnelchanges")
            .build();
    }

    @Bean
    GroupedOpenApi clubSeat() {
        return GroupedOpenApi.builder()
            .group("社团-座位安排")
            .packagesToScan("team.project.module.club.seat")
            .build();
    }

    @Bean
    GroupedOpenApi fileStorage() {
        return GroupedOpenApi.builder()
            .group("文件存储")
            .packagesToScan("team.project.module.filestorage")
            .build();
    }

    @Bean
    GroupedOpenApi department() {
        return GroupedOpenApi.builder()
            .group("院系管理")
            .pathsToMatch("/department/*")
            .build();
    }
}
