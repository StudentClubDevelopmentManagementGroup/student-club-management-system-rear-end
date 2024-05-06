package team.project.module.club.seat.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.queryparam.QueryParam;
import team.project.base.controller.response.Response;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.club.seat.internal.model.request.AddSeatReq;
import team.project.module.club.seat.internal.model.request.DelSeatReq;
import team.project.module.club.seat.internal.model.request.UpdateSeatReq;
import team.project.module.club.seat.internal.model.view.ClubMemberInfoVO;
import team.project.module.club.seat.internal.model.view.SeatVO;
import team.project.module.club.seat.internal.service.SeatService;

import java.util.List;

@Tag(name="座位安排")
@RestController
@RequestMapping("/club/seat")
public class SeatController {

    @Autowired
    AuthServiceI authService;

    @Autowired
    SeatService seatService;

    @Operation(summary="添加座位")
    @PostMapping("/add")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object add(@Valid @RequestBody AddSeatReq req) {

        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "不是该社团的负责人，不能给该社团添加座位");

        List<SeatVO> result = seatService.addSeat(arrangerId, req);
        return new Response<>(ServiceStatus.CREATED).data(result);
    }

    @Operation(summary="更新座位信息", description="""
     - 修改部分信息：x、y、description 传修改后的值（传 null 则不修改）
     - 分配座位：owner_id 传学号/工号，且 unset_owner 传 false 或 null
     - 将座位置空：owner_id 传 null，且 unset_owner 传 true
    """)
    @PostMapping("/update")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object update(@Valid @RequestBody UpdateSeatReq req) {
        UpdateSeatReq.validate(req);

        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "不是该社团的负责人，不能更新该社团的座位信息");

        seatService.updateSeat(arrangerId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="查看座位表")
    @GetMapping("/view")
    Object view(@NotNull @ClubIdConstraint @RequestParam("club_id") Long clubId) {
        List<SeatVO> result = seatService.view(clubId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="删除座位")
    @PostMapping("/del")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object del(@Valid @RequestBody DelSeatReq req) {

        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "不是该社团的负责人，不能删除该社团的座位");

        seatService.deleteSeat(req);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="查询没有座位的成员（分页查询）")
    @GetMapping("/members/no_seat")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object membersNoSeat(
        @NotNull(message="未指定社团id")
        @ClubIdConstraint
        @RequestParam("club_id") Long clubId,

        @QueryParam PagingQueryReq pageReq
    ) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, clubId, "不是该社团的负责人，不能查询该社团没有座位的成员");

        PageVO<ClubMemberInfoVO> result = seatService.membersNoSeat(clubId, pageReq);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }
}
