package team.project.module.user.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.UserAccountService;

@Tag(name="用户身份验证、账号管理")
@RestController
public class UserAccountController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserAccountService service;

    @Operation(summary="注册账号")
    @PostMapping("/user/register")
    Object register(@Valid @RequestBody RegisterReq req) {
        service.register(req);
        return new Response<>(ServiceStatus.CREATED).statusText("注册成功");
    }

    @Operation(summary="注销账号")
    @PostMapping("/user/cancel_account")
    Object cancelAccount(
        @RequestParam("user_id") String userId,
        @RequestParam("pwd")     String password
    ) {
        service.cancelAccount(userId, password);
        return new Response<>(ServiceStatus.SUCCESS).statusText("销号成功");
    }

    @Operation(summary="使用密码登录")
    @PostMapping("/user/login/password")
    Object loginWithPassword(
        @RequestParam("user_id") String userId,
        @RequestParam("pwd")     String password
    ) {
        UserInfoVO userInfo = service.login(userId, password);
        return new Response<>(ServiceStatus.SUCCESS).statusText("登录成功").data(userInfo);
    }

    @Operation(summary="使用邮箱登录")
    @PostMapping("/user/login/email")
    Object loginWithEmail() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary="登出")
    @PostMapping("/user/logout")
    Object logout() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary="获取所有的用户账号信息")
    @GetMapping("/user/select_all")
    Object selectAll(
        @RequestParam("page_num")  int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return new Response<>(ServiceStatus.SUCCESS).data(service.selectAll(pageNum, pageSize));
    }
}
