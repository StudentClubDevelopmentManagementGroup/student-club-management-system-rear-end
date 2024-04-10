package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.export.model.annotation.UserIdConstraint;
import team.project.module.user.internal.model.request.UserIdAndPasswordReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.LoginService;

import java.util.HashMap;
import java.util.Map;

@Tag(name="登录")
@RestController
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LoginService service;

    @Operation(summary="使用密码登录")
    @PostMapping("/user/login/password")
    Object loginWithPassword(@Valid @RequestBody UserIdAndPasswordReq req) {

        String userId = req.getUserId();
        String password = req.getPassword();

        UserInfoVO userInfo = service.login(userId, password);

        StpUtil.login(userId);
        String token = StpUtil.getTokenValueByLoginId(userId);
        StpUtil.getSessionByLoginId(StpUtil.getLoginId()).set("user_id", userId);

        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("user_info", userInfo);
        loginResult.put("token", token);

        return new Response<>(ServiceStatus.SUCCESS).statusText("登录成功").data(loginResult);
    }

    @Operation(summary="使用邮箱登录", hidden=true)
    @PostMapping("/user/login/email")
    Object loginWithEmail() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary="登出")
    @PostMapping("/user/logout")
    @SaCheckLogin
    Object logout() {
        String userId = (String)StpUtil.getSession().getLoginId();
        StpUtil.logout(userId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("登出成功");
    }
}
