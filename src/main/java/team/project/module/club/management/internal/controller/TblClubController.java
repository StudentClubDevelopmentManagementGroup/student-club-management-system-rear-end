package team.project.module.club.management.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.query.ClubInfoQO;
import team.project.module.club.management.internal.model.request.ListClubInfoReq;
import team.project.module.club.management.internal.model.request.OneClubInfoReq;
import team.project.module.club.management.internal.model.request.TblClubIntroductionReq;
import team.project.module.club.management.internal.model.view.selectClubVO;
import team.project.module.club.management.internal.service.TblClubService;

@Tag(name = "社团管理")
@RestController
public class TblClubController {
    @Autowired
    AuthServiceI authService;

    @Autowired
    TblClubService service;

    @Operation(summary = "创建基地")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    @PostMapping("/club/add")
    Object createClub(@Valid @RequestBody OneClubInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有超级管理员能创建基地");

        service.createClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary = "查询基地基础信息")
    @PostMapping("/club/select")
    Object selectClub(@Valid @RequestBody ListClubInfoReq req) {
        ClubInfoQO newQO = new ClubInfoQO(req.getDepartmentId(), req.getName(), req.getPageNum(), req.getSize());
        PageVO<selectClubVO> result;
        result = service.selectByCriteria(newQO);
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "删除基地")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    @PostMapping("/club/del")
    Object deleteClub(@Valid @RequestBody OneClubInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有超级管理员能删除基地");

        int result = service.deleteClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功"+"，删除了"+result+"条社团成员数据");
    }

    @Operation(summary = "撤销删除基地")
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    @PostMapping("/club/undelete")
    Object recoverClub(@Valid @RequestBody OneClubInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有超级管理员能撤销删除基地");

        service.recoverClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("撤销删除成功");
    }

    @Operation(summary = "基地开放招人")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/recruitment/open")
    Object reuseClub(@Valid @RequestBody OneClubInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有社团负责人能开放招人");

        service.reuseClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("修改成功");
    }

    @Operation(summary = "基地停止招人")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/recruitment/close")
    Object deactivateClub(@Valid @RequestBody OneClubInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有社团负责人能停止招人");

        service.deactivateClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("修改成功");
    }

    @Operation(summary = "基地总信息，包括人数、负责人以及是否开放招新", description = """
            包括基地名称、院系名称、院系id、基地编号、基地人数、招新状态、负责人、是否删除
            """)
    @SaCheckRole(AuthRole.SUPER_ADMIN)
    @PostMapping("/club/select_all")
    Object selectAll(@Valid @RequestBody ListClubInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有超级管理员能查询1.基地总信息，包括人数、负责人以及是否开放招新");

        ClubInfoQO newQO = new ClubInfoQO(req.getDepartmentId(), req.getName(), req.getPageNum(), req.getSize());
        PageVO<ClubMsgDTO> result;
        result = service.findAll(newQO);
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "修改社团简介")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/update_introduction")
    Object updateIntroduction(@Valid @RequestBody TblClubIntroductionReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireSuperAdmin(arrangerId, "只有社团负责人能修改社团简介");

        service.updateIntroduction(req.getDepartmentId(), req.getName(), req.getIntroduction());
        return new Response<>(ServiceStatus.SUCCESS).statusText("修改成功");
    }
}
