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
    GroupedOpenApi clubAnnouncement() {
        return GroupedOpenApi.builder()
            .group("社团-公告")
            .packagesToScan("team.project.module.club.announcement")
            .build();
    }

    @Bean
    GroupedOpenApi attendance() {
        return GroupedOpenApi.builder()
            .group("社团-签到签退")
            .packagesToScan("team.project.module.club.attendance")
            .build();
    }

    @Bean
    GroupedOpenApi duty() {
        return GroupedOpenApi.builder()
            .group("社团-值日")
            .packagesToScan("team.project.module.club.duty")
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
            .group("社团-座位")
            .packagesToScan("team.project.module.club.seat")
            .build();
    }

    @Bean
    GroupedOpenApi department() {
        return GroupedOpenApi.builder()
            .group("院系管理")
            .packagesToScan("team.project.module.department")
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
    GroupedOpenApi user() {
        return GroupedOpenApi.builder()
            .group("用户（注册、登录、个人账户信息管理）")
            .packagesToScan("team.project.module.user")
            .pathsToExclude("/user/management/**")
            .build();
    }

    @Bean
    GroupedOpenApi userManagement() {
        return GroupedOpenApi.builder()
            .group("用户管理（管理员对所有账号的管理）")
            .packagesToScan("team.project.module.user")
            .pathsToMatch("/user/management/**")
            .build();
    }
}
