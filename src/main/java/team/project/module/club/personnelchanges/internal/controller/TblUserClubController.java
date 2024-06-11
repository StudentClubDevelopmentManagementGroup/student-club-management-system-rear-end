package team.project.module.club.personnelchanges.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.personnelchanges.internal.model.query.ClubMemberInfoQO;
import team.project.module.club.personnelchanges.internal.model.request.ClubMemberInfoReq;
import team.project.module.club.personnelchanges.internal.model.request.UserClubReq;
import team.project.module.club.personnelchanges.internal.service.TblUserClubService;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Tag(name = "社团人员管理")
@RestController
public class TblUserClubController {

    @Autowired
    TblUserClubService ucService;

    @Autowired
    AuthServiceI authService;
    @Operation(summary = "基地设置负责人")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/member/set_manager")
    Object setManager(@Valid @RequestBody UserClubReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能设置负责人");

        ucService.setClubManager(req.getUserId(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("设置成功");
    }

    @Operation(summary = "基地撤销负责人")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/member/unset_manager")
    Object quashManager(@Valid @RequestBody UserClubReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能基地撤销负责人");

        ucService.quashClubManager(req.getUserId(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("撤销成功");
    }

    @Operation(summary = "添加基地成员")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/member/add")
    Object createMember(@Valid @RequestBody UserClubReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能添加基地成员");

        ucService.createMember(req.getUserId(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("添加成功");
    }

    @Operation(summary = "撤销基地成员")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/member/del")
    Object quashMember(@Valid @RequestBody UserClubReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能撤销基地成员");

        ucService.quashMember(req.getUserId(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("撤销成功");
    }

    @Operation(summary = "查询基地所有成员信息")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/member/select_all")
    Object selectMember(@Valid @RequestBody ClubMemberInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能查询基地所有成员信息");

        ClubMemberInfoQO QO = new ClubMemberInfoQO(req.getDepartmentId(), req.getName(), req.getClubId(), req.getPageNum(), req.getSize());
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(ucService.selectClubMemberInfo(QO));
    }

    @Operation(summary = "查询该用户是否是该社团成员")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/member/select_member")
    Object selectMember(@Valid @RequestBody UserClubReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能查询该用户是否是该社团成员");

        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(ucService.selectTheMember(req.getUserId(), req.getClubId()));
    }

    @Operation(summary = "查询该用户在所有社团的身份")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    @PostMapping("/club/member/select_member_all_club_info")
    Object selectMemberAllClubInfo(@NotNull(message = "学号工号不能为空")  @UserIdConstraint @RequestParam("user_id") String userId) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有超级管理员能查询基地所有成员信息");

        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(ucService.selectMemberAllClubInfo(userId));
    }
}
