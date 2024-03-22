package team.project.module.useraccount.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.useraccount.internal.service.LoginService;

@Tag(name="用户身份验证、账号管理")
@RestController
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LoginService service;

    @Operation(summary="使用密码登录")
    @PostMapping("/user/login/password")
    Object loginWithPassword(
        @RequestParam("user_id") String userId,
        @RequestParam("pwd")     String password
    ) {
        if (service.login(userId, password)) {
            return new Response<>(ServiceStatus.SUCCESS).statusText("登录成功");
        }
        else {
            return new Response<>(ServiceStatus.UNAUTHORIZED).statusText("登录失败");
        }
    }

    @Operation(summary="使用邮箱登录")
    @PostMapping("/user/login/email")
    Object loginWithEmail() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }
}
