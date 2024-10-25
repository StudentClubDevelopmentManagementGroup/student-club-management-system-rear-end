package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.internal.model.request.UserIdAndPasswordReq;
import team.project.module.user.internal.service.RegisterService;

@Tag(name="注册")
@RestController
@RequestMapping("/user")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @Operation(summary="注册账号")
    @PostMapping("/register")
    Object register(@Valid @RequestBody RegisterReq req) {
        registerService.register(req);
        return new Response<>(ServiceStatus.CREATED).statusText("注册成功");
    }

    @Operation(summary="注册账号（发送验证码到邮箱，确保邮箱可用）")
    @PostMapping("/register/email/send_code")
    Object sendCodeEmail(
        @Email(message="邮箱格式不正确")
        @RequestParam("email") String email
    ) {
        registerService.sendCodeEmail(email);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="注销账号")
    @PostMapping("/unregister")
    @SaCheckLogin
    Object unregister(@Valid @RequestBody UserIdAndPasswordReq req) {
        registerService.unregister(req.getUserId(), req.getPassword());
        return new Response<>(ServiceStatus.SUCCESS).statusText("销号成功");
    }
}
