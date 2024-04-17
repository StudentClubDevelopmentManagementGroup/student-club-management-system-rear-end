package team.project.module.club.seat.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
import team.project.module.club.seat.internal.model.view.UserInfoVO;
import team.project.module.club.seat.internal.service.SeatService;

import java.util.HashMap;
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

    @Operation(summary="更新座位信息", description="""
         - 修改部分信息：x、y、description 传 null 则不修改
         - 分配座位：owner_id 不能传学号/工号，unset_owner 传 false 或 null
         - 将座位置空：owner_id 传 null，且 unset_owner 传 true
        """)
    @PostMapping("/club/seat/update")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object update(@Valid @RequestBody UpdateSeatReq req) {

        for (UpdateSeatReq.ToUpdateSeatInfo seat : req.getSeatList()) {
            if (seat.getUnsetOwner() && seat.getOwnerId() != null) {
                return new Response<>(ServiceStatus.BAD_REQUEST).statusText("如果要将座位置空，则不该指明所属者的学号/工号");
            }
        }

        String arrangerId = (String)StpUtil.getSession().getLoginId();
        seatService.updateSeat(arrangerId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="查看座位表")
    @GetMapping("/club/seat/view")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    Object view(@NotNull @ClubIdConstraint @RequestParam("club_id") Long clubId) {
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


    @Operation(summary="查询没有座位的社团成员")
    @GetMapping("/club/seat/members/no_seat")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object membersNoSeat(
        @NotNull(message="未指定社团id") @ClubIdConstraint
        @RequestParam("club_id") Long clubId,

        @Min(value=1, message="分页查询指定当前页码不能小于 1")
        @RequestParam(value="page_num", required=false)  Long pageNum,

        @Min(value=1, message="每页大小最小不能小于 1")
        @RequestParam(value="page_size", required=false) Long pageSize
    ) {
        if ((pageNum != null && pageSize == null) || (pageNum == null && pageSize != null)) {
            return new Response<>(ServiceStatus.BAD_REQUEST).statusText("分页查询需同时指定页码与页大小");
        }

        String arrangerId = (String)StpUtil.getSession().getLoginId();
        List<UserInfoVO> result = seatService.membersNoSeat(arrangerId, clubId);

        HashMap<String, Object> resultMap = new HashMap<>();

        if (pageNum != null) { /* 伪分页 */
            resultMap.put("current_page", pageNum);
            resultMap.put("page_size",    pageSize);
            resultMap.put("total_pages",  (long)Math.ceil((double) result.size() / pageSize));
            resultMap.put("total_item",   (long)result.size());
            result = result.subList(
                Math.min(result.size(), (int)((pageNum - 1) * pageSize)),
                Math.min(result.size(), (int)(pageNum * pageSize))
            );
        }
        else {
            resultMap.put("current_page", 1L);
            resultMap.put("page_size",    (long)result.size());
            resultMap.put("total_pages",  1L);
            resultMap.put("total_item",   (long)result.size());
        }
        resultMap.put("records", result);

        return new Response<>(ServiceStatus.SUCCESS).data(resultMap);
    }
}
