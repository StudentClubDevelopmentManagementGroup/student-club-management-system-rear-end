package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.queryparam.QueryParam;
import team.project.base.controller.response.Response;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.user.export.model.annotation.UserIdConstraint;
import team.project.module.user.internal.model.request.SearchUserReq;
import team.project.module.user.internal.model.request.UserIdAndPasswordReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.UserInfoService;

@Tag(name="管理员对所有账号的管理")
@RestController
@RequestMapping("/user/management")
public class UserManagementController {

    @Autowired
    UserInfoService userInfoService;

    /* -- 查询操作 -- */

    @Operation(summary="查询指定用户账号信息")
    @GetMapping("/select")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object selectOne(@NotNull @UserIdConstraint @RequestParam("user_id") String userId) {

        UserInfoVO userInfo = userInfoService.selectUserInfo(userId);

        if (userInfo == null) {
            return new Response<>(ServiceStatus.NOT_FOUND).statusText("查询不到信息");
        }

        return new Response<>(ServiceStatus.SUCCESS).data(userInfo);
    }

    @Operation(summary="查询所有用户账号信息（分页查询）")
    @GetMapping("/select/all")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object selectAll(@QueryParam PagingQueryReq pageReq) {
        return new Response<>(ServiceStatus.SUCCESS).data(userInfoService.selectUserInfo(pageReq));
    }

    @Operation(summary="搜索用户，返回账号信息（分页查询、模糊查询）", description="""
        user_id：学号/工号检索关键字，查询学号/工号中包含关键字的用户（传 null 或 "" 表示全匹配）
        user_name：姓名检索关键字，查询名字中包含关键字的用户（传 null 或 "" 表示全匹配）
        department_id：院系编号，查询指定院系的用户（传 null 或 0 表示全匹配）
        page_num：分页查询，当前页码
        page_size：分页查询，页大小
    """)
    @GetMapping("/search")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object search(
        @Valid @QueryParam SearchUserReq  searchReq,
        @Valid @QueryParam PagingQueryReq pageReq
    ) {
        return new Response<>(ServiceStatus.SUCCESS).data(userInfoService.searchUserInfo(pageReq, searchReq));
    }

    /* -- 更改操作 -- */

    @Operation(summary="超级管理员强制设置指定用户的密码")
    @PostMapping("/set/password")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object setPassword(@Valid @RequestBody UserIdAndPasswordReq req) {
        userInfoService.setPassword(req);
        return new Response<>(ServiceStatus.SUCCESS);
    }
}
