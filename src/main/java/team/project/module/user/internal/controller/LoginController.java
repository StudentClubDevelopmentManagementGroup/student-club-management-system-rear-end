package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.LoginService;

@Tag(name="登录")
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
        UserInfoVO userInfo = service.login(userId, password);
        StpUtil.login(userId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("登录成功").data(userInfo);
    }

    @Operation(summary="使用邮箱登录", hidden=true)
    @PostMapping("/user/login/email")
    Object loginWithEmail() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary="登出")
    @PostMapping("/user/logout")
    @SaCheckLogin
    Object logout(@RequestParam("user_id") String userId) {
        StpUtil.logout(userId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("登出成功");
    }
}
