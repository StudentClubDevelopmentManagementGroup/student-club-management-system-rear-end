package team.project.module.user.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.service.UserInfoService;

@Tag(name="个人账户信息管理")
@RestController
@RequestMapping("/user_info")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @Operation(summary="查询自己的账号信息")
    @GetMapping("/select")
    @SaCheckLogin
    Object selectSelf() {
        String userId = (String)( StpUtil.getLoginId() );

        UserInfoVO userInfo = userInfoService.selectUserInfo(userId);

        if (userInfo == null) { /* 将“已登录的用户不能查询到自己的账号信息”视为服务端异常 */
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response<>(ServiceStatus.SUCCESS).data(userInfo);
    }
}
