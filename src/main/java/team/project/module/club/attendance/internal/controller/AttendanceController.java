package team.project.module.club.attendance.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.ApplyAttendanceReq;
import team.project.module.club.attendance.internal.model.request.DayCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckoutReq;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
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

    @Operation(summary="社团成员签退，时间格式为(2024-04-15 13:01:33)")
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


    @Operation(summary="负责人帮社团成员补签，时间格式为(2024-04-15 13:01:33)")
    @PostMapping("/applyAttendance")
    public Object makeUpAttendance(@RequestBody /* TODO jsr303 */ ApplyAttendanceReq applyAttendanceReq) {
        AttendanceInfoVO attendanceInfoVO = attendanceService.makeUpAttendance(applyAttendanceReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("补签成功")
                .data(attendanceInfoVO);
    }



    @Operation(summary="查询社团成员本周签到时长，返回秒")
    @GetMapping("/totalWeekSeconds")
    public Object getTotalWeekSeconds(
            @RequestParam("userId") String userId,
            @RequestParam("clubId") Long clubId){

        Long totalWeekSeconds = attendanceService.getTotalWeekSeconds(userId, clubId);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(totalWeekSeconds);
    }


    @Operation(summary="查询社团成员一个月的打卡时长，格式：年份（2024），月份（3），返回秒")
    @GetMapping("/totalMonthSeconds")
    public Object getTotalMonthSeconds(
            @RequestParam("userId") String userId,
            @RequestParam("clubId") Long clubId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        Long totalMonthSeconds = attendanceService.getTotalMonthSeconds(userId, clubId, year, month);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(totalMonthSeconds);
    }

    @Operation(summary="查询社团成员一年打卡时长，格式：年份（2024），返回秒")
    @GetMapping("/totalYearSeconds")
    public Object getTotalYearSeconds(
            @RequestParam("userId") String userId,
            @RequestParam("clubId") Long clubId,
            @RequestParam("year") int year){

        Long totalMonthSeconds = attendanceService.getTotalYearSeconds(userId, clubId, year);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(totalMonthSeconds);
    }


}
