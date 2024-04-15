package team.project.module.club.attendance.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.DayCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckoutReq;
import team.project.module.club.attendance.internal.service.AttendanceService;

import java.util.List;

@Tag(name="签到签退模块")
@RestController
@RequestMapping("/clockIn")
public class AttendanceController {

    @Autowired //注入
    private AttendanceService attendanceService;


    @Operation(summary="社团成员签到,时间格式为(2024-04-15 13:01:33)")
    @PostMapping("/checkIn")
    public Object userCheckIn(@RequestBody /* TODO jsr303 */ UserCheckInReq userCheckinReq) {
        boolean success = attendanceService.userCheckIn(userCheckinReq);
        if (success) {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("签到成功");
        } else {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR)
                    .statusText("签到失败");
        }
    }

    @Operation(summary="查询社团成员当天的签到记录")
    @PostMapping("/getDayCheckIn")
    public Object getCheckInRecords(@RequestBody DayCheckInReq dayCheckInReq ) {
        // 调用服务层方法执行查询当天签到记录的逻辑
        List<AttendanceDO> dayCheckInRecords = attendanceService.getDayCheckIn(dayCheckInReq);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(dayCheckInRecords);
    }

    @Operation(summary="社团成员签退")
    @PatchMapping ("/checkout")
    public Object userCheckout(@RequestBody UserCheckoutReq userCheckoutReq) {
        boolean success = attendanceService.userCheckOut(userCheckoutReq);
        if (success) {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("签退成功");
        } else {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY)
                    .statusText("签退失败，没有该签到记录");
        }
    }
}
