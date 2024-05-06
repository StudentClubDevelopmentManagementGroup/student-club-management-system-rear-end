package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.queryparam.QueryParam;
import team.project.base.controller.response.Response;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.user.export.model.annotation.UserIdConstraint;
import team.project.module.user.internal.model.request.SearchUserReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.UserInfoService;

@Tag(name="用户信息管理")
@RestController
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @Operation(summary="查询自己的账号信息")
    @GetMapping({"/user_info/select_self"})
    @SaCheckLogin
    Object selectSelf() {
        String userId = (String)( StpUtil.getLoginId() );

        UserInfoVO userInfo = userInfoService.selectUserInfo(userId);

        if (userInfo == null) { /* 将“已登录的用户不能查询到自己的账号信息”视为服务端异常 */
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response<>(ServiceStatus.SUCCESS).data(userInfo);
    }

    @Operation(summary="查询指定用户账号信息")
    @GetMapping("/user_info/select_one")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object selectOne(@NotNull @UserIdConstraint @RequestParam("user_id") String userId) {

        UserInfoVO userInfo = userInfoService.selectUserInfo(userId);

        if (userInfo == null) {
            return new Response<>(ServiceStatus.NOT_FOUND).statusText("查询不到信息");
        }

        return new Response<>(ServiceStatus.SUCCESS).data(userInfo);
    }

    @Operation(summary="查询所有用户账号信息（分页查询）")
    @GetMapping("/user_info/select_all")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object selectAll(@QueryParam PagingQueryReq pageReq) {
        return new Response<>(ServiceStatus.SUCCESS).data(userInfoService.selectUserInfo(pageReq));
    }

    @Operation(summary="搜索用户，返回账号信息（分页查询、模糊查询）", description="""
     - 学号/工号传 null 或 "" 表示全匹配
     - 姓名传 null 或 "" 表示全匹配
     - 院系 id 传 null 或 0 表示全匹配
    """)
    @GetMapping("/user_info/search")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object selectAll(
        @Valid @QueryParam SearchUserReq  searchReq,
        @Valid @QueryParam PagingQueryReq pageReq
    ) {
        return new Response<>(ServiceStatus.SUCCESS).data(userInfoService.searchUsers(pageReq, searchReq));
    }
}
