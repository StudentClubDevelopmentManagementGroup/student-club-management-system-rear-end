package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.response.Response;
import team.project.base.controller.queryparam.QueryParam;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.user.export.model.annotation.UserIdConstraint;
import team.project.base.model.request.PagingQueryReq;
import team.project.module.user.internal.model.request.QueryUserInfoReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.UserInfoService;

@Tag(name="用户信息管理")
@RestController
public class UserInfoController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserInfoService service;
    @Operation(summary="查询自己的账号信息")
    @GetMapping("/user_info/self")
    @SaCheckLogin
    Object selectSelf() {
        String userId = (String)( StpUtil.getSession().getLoginId() );

        UserInfoVO userInfo = service.selectUserInfo(userId);

        if (userInfo == null) { /* 将“已登录的用户不能查询到自己的账号信息”视为服务端异常 */
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response<>(ServiceStatus.SUCCESS).data(userInfo);
    }

    @Operation(summary="查询指定用户账号信息")
    @GetMapping("/user_info/select_one")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object selectOne(@NotNull @UserIdConstraint @RequestParam("user_id") String userId) {

        UserInfoVO userInfo = service.selectUserInfo(userId);

        if (userInfo == null) {
            return new Response<>(ServiceStatus.NOT_FOUND).statusText("查询不到信息");
        }

        return new Response<>(ServiceStatus.SUCCESS).data(userInfo);
    }

    @Operation(summary="查询所有用户账号信息（分页查询）", hidden=true)
    @GetMapping("/user_info/all")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object pagingQuery(@QueryParam PagingQueryReq pageReq) {
        return new Response<>(ServiceStatus.SUCCESS).data(
            service.pagingQueryUserInfo(pageReq)
        );
    }

    @Operation(summary="查询用户账号信息（模糊查询，分页查询）")
    @GetMapping("/user_info/query")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object queryUserInfo(
        @QueryParam QueryUserInfoReq queryReq,
        @QueryParam PagingQueryReq   pageReq
    ) {
        return new Response<>(ServiceStatus.SUCCESS).data(
            service.pagingQueryUserInfo(queryReq, pageReq)
        );
    }
}
