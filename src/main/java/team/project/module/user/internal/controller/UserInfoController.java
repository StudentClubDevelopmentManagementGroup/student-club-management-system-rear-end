package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.model.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.UserInfoService;

@Tag(name="用户信息管理")
@RestController
public class UserInfoController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserInfoService service;

    @Operation(summary="分页查询用户账号信息（要求 SUPER_ADMIN 角色）")
    @GetMapping("/user_info/paging_query")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    Object selectAll(
        @Min(value=1, message="分页查询指定当前页码不能小于 1")
        @RequestParam("page_num")  Long pageNum,

        @Min(value=1, message="每页大小最小不能小于 1")
        @RequestParam("page_size") Long pageSize
    ) {
        PageVO<UserInfoVO> userInfo = service.pagingQueryUserInfo(pageNum, pageSize);
        return new Response<>(ServiceStatus.SUCCESS).data(userInfo);
    }
}
