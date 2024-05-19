package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.export.model.annotation.UserIdConstraint;
import team.project.module.user.internal.model.request.UserIdAndCodeReq;
import team.project.module.user.internal.model.request.UserIdAndPasswordReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.LoginService;

import java.util.HashMap;
import java.util.Map;

@Tag(name="登录")
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Operation(summary="使用密码登录")
    @PostMapping("/login/password")
    Object loginWithPassword(@Valid @RequestBody UserIdAndPasswordReq req) {

        String userId = req.getUserId();
        String password = req.getPassword();

        UserInfoVO userInfo = loginService.login(userId, password);
        if (userInfo == null) {
            /* 依前端要求，登录失败返回 400 状态码 */
            return new Response<>(ServiceStatus.BAD_REQUEST).statusText("用户不存在或密码错误");
        }

        StpUtil.login(userId);

        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("user_info", userInfo);
        loginResult.put("token", StpUtil.getTokenInfo().getTokenValue());

        return new Response<>(ServiceStatus.SUCCESS).statusText("登录成功").data(loginResult);
    }

    @Operation(summary="使用邮箱登录（发送验证码）")
    @PostMapping("/login/send_code/email")
    Object sendCode(
        @NotBlank(message="学号/工号不能为空") @UserIdConstraint
        @RequestParam("user_id") String userId
    ) {
        loginService.sendCodeByEmail(userId);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="使用邮箱登录")
    @PostMapping("/login/email")
    Object loginWithEmail(@Valid @RequestBody UserIdAndCodeReq req) {

        UserInfoVO userInfo = loginService.login(req);

        if (userInfo == null) {
            /* 依前端要求，登录失败返回 400 状态码 */
            return new Response<>(ServiceStatus.BAD_REQUEST).statusText("验证码不正确");
        }

        StpUtil.login(req.getUserId());

        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("user_info", userInfo);
        loginResult.put("token", StpUtil.getTokenInfo().getTokenValue());

        return new Response<>(ServiceStatus.SUCCESS).statusText("登录成功").data(loginResult);
    }

    @Operation(summary="登出")
    @PostMapping("/logout")
    @SaCheckLogin
    Object logout() {
        String userId = (String)( StpUtil.getLoginId() );
        StpUtil.logout(userId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("登出成功");
    }
}
