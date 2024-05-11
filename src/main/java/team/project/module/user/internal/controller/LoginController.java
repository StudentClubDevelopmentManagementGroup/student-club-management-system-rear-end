package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
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
        SaTokenInfo token = StpUtil.getTokenInfo();

        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("user_info", userInfo);
        loginResult.put("token", token.getTokenValue());

        return new Response<>(ServiceStatus.SUCCESS).statusText("登录成功").data(loginResult);
    }

    @Operation(summary="使用邮箱登录", hidden=true)
    @PostMapping("/login/email")
    Object loginWithEmail() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
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
