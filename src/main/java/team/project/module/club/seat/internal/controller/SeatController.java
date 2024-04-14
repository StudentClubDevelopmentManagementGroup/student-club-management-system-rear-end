package team.project.module.club.seat.internal.controller;

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
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.club.seat.internal.model.request.*;
import team.project.module.club.seat.internal.model.view.SeatVO;
import team.project.module.club.seat.internal.service.SeatService;

import java.util.List;

@Tag(name="座位安排")
@RestController
public class SeatController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeatService seatService;

    @Operation(summary="添加座位")
    @PostMapping("/club/seat/add")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object add(@Valid @RequestBody AddSeatReq req) {
        String arrangerId = (String)StpUtil.getSession().getLoginId();
        List<SeatVO> result = seatService.addSeat(arrangerId, req);
        return new Response<>(ServiceStatus.CREATED).data(result);
    }

    @Operation(summary="分配座位给社团成员", description="如果传入 owner_id 为 null，则将座位设为空座")
    @PostMapping("/club/seat/set_owner")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object setOwner(@Valid @RequestBody SetOwnerReq req) {
        String arrangerId = (String)StpUtil.getSession().getLoginId();
        seatService.setOwner(arrangerId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="更新座位信息", description="如果传入的属性值为 null 则不修改")
    @PostMapping("/club/seat/update_info")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object updateInfo(@Valid @RequestBody UpdateSeatInfoReq req) {
        String arrangerId = (String)StpUtil.getSession().getLoginId();
        seatService.updateSeatInfo(arrangerId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="查看座位表")
    @GetMapping("/club/seat/view")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    Object view(@ClubIdConstraint @RequestParam("club_id") Long clubId) {
        String userId = (String)StpUtil.getSession().getLoginId();
        List<SeatVO> result = seatService.view(userId, clubId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="删除座位")
    @PostMapping("/club/seat/del")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object del(@Valid @RequestBody DelSeatReq req) {
        String arrangerId = (String)StpUtil.getSession().getLoginId();
        seatService.deleteSeat(arrangerId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }
}
