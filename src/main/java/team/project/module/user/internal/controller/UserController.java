package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.export.model.enums.Role;
import team.project.module.user.internal.model.enums.UserRole;
import team.project.module.user.internal.model.request.RegisterReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.UserService;

@Tag(name="用户身份验证、账号管理")
@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService service;

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
    Object logout(@RequestParam("user_id") String userId) {
        StpUtil.logout(userId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("登出成功");
    }

    @Operation(summary="分页查询用户账号信息")
    @GetMapping("/user_info/paging_query")
    @SaCheckRole("ljh")
    Object selectAll(
        @RequestParam("page_num")  int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return new Response<>(ServiceStatus.SUCCESS).data(service.pagingQueryUserInfo(pageNum, pageSize));
    }
}
