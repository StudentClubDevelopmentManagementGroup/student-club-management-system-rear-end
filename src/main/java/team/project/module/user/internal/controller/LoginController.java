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
import team.project.module.user.internal.model.request.ResetReq;
import team.project.module.user.internal.model.request.UserIdAndCodeReq;
import team.project.module.user.internal.model.request.UserIdAndPasswordReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.LoginService;
import team.project.module.user.internal.service.UserInfoService;

import java.util.HashMap;
import java.util.Map;

/* TODO ljh_TODO: 配置 sa-token 多段登录，以免登录小程序后将 PC 顶下线 */

@Tag(name="登录")
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    UserInfoService userInfoService;

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

    @Operation(summary="使用邮箱登录（发送验证码到邮箱）")
    @PostMapping("/login/email/send_code")
    Object sendCodeEmail(
        @NotBlank(message="学号/工号不能为空") @UserIdConstraint
        @RequestParam("user_id") String userId
    ) {
        loginService.sendCodeEmail(userId);
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

    @Operation(summary="使用邮箱修改密码")
    @PostMapping("/password/reset")
    Object resetWithEmail(@Valid @RequestBody ResetReq req) {

        UserIdAndCodeReq userIdAndCodeReq = new UserIdAndCodeReq(req.getUserId(),req.getCode());
        UserInfoVO userInfo = loginService.login(userIdAndCodeReq);

        if (userInfo == null) {
            /* 依前端要求，登录失败返回 400 状态码 */
            return new Response<>(ServiceStatus.BAD_REQUEST).statusText("验证码不正确");
        }

        UserIdAndPasswordReq userIdAndPasswordReq = new UserIdAndPasswordReq(req.getUserId(),req.getPwd());
        userInfoService.setPassword(userIdAndPasswordReq);
        return new Response<>(ServiceStatus.SUCCESS);
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
