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
import team.project.module.club.seat.internal.model.request.AddSeatReq;
import team.project.module.club.seat.internal.model.request.DelSeatReq;
import team.project.module.club.seat.internal.model.request.SetSeatReq;
import team.project.module.club.seat.internal.service.SeatService;

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
        String userId = (String)StpUtil.getSession().getLoginId();
        seatService.addSeat(userId, req);
        return new Response<>(ServiceStatus.CREATED);
    }

    @Operation(summary="安排座位")
    @GetMapping("/club/seat/set")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object set(@Valid @RequestBody SetSeatReq req) {
        String userId = (String)StpUtil.getSession().getLoginId();
        seatService.setSeat(userId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="查看座位表")
    @GetMapping("/club/seat/view")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    Object view(
        /* TODO jsr303 */ @RequestParam("club_id") Long clubId
    ) {
        String userId = (String)StpUtil.getSession().getLoginId();
        return new Response<>(ServiceStatus.SUCCESS).data(seatService.view(userId, clubId));
    }

    @Operation(summary="删除座位")
    @PostMapping("/club/seat/del")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object del(@Valid @RequestBody DelSeatReq req) {
        String userId = (String)StpUtil.getSession().getLoginId();
        seatService.deleteSeat(userId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }
}
