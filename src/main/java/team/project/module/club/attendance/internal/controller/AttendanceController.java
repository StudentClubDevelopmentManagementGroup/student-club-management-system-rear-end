package team.project.module.club.attendance.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import team.project.module.club.attendance.internal.service.AttendanceService;
import java.util.List;



@Tag(name="签到签退模块")
@RestController
@RequestMapping("/clockIn")
public class AttendanceController {

    @Autowired //注入
    private AttendanceService attendanceService;

    @Operation(summary="查询社团成员当天最新的签到记录")
    @GetMapping("/getLatestCheckInRecord")
    public Object getLatestCheckInRecord(
            @RequestParam("userId") String userId,
            @RequestParam("clubId") Long clubId) {
        // 调用服务层方法执行查询当天签到记录的逻辑
        AttendanceInfoVO attendanceInfoVO = attendanceService.getLatestCheckInRecord(userId,clubId);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(attendanceInfoVO);
    }

    @Operation(summary="社团成员签到,时间格式为(2024-04-15 13:01:33)")
    @PostMapping("/checkInTest")
    public Object userCheckInTest(@RequestBody /* TODO jsr303 */ UserCheckInReq userCheckinReq) {
        // 查询当天最新的签到记录
        AttendanceInfoVO latestCheckInRecord = attendanceService.getLatestCheckInRecord(userCheckinReq.getUserId(), userCheckinReq.getClubId());

        // 如果最新签到记录存在且签退时间不为空，则表示上一次签到未签退
        if (latestCheckInRecord != null && latestCheckInRecord.getCheckoutTime() == null) {
            return new Response<>(ServiceStatus.BAD_REQUEST)
                    .statusText("签到失败，上一次签到未签退");
        }

        // 如果最新签到记录不存在或签退时间不为空，则可以进行签到操作
        AttendanceInfoVO attendanceInfoVO = attendanceService.userCheckIn(userCheckinReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("签到成功")
                .data(attendanceInfoVO);
    }


    @Operation(summary="社团成员签退，时间格式为(2024-04-15 13:01:33)")
    @PatchMapping ("/checkoutTest")
    public Object userCheckoutTest(@RequestBody UserCheckoutReq userCheckoutReq) {
        AttendanceInfoVO attendanceInfoVO = attendanceService.userCheckOutTest(userCheckoutReq);
        if(attendanceInfoVO != null){
            return new Response<>(ServiceStatus.SUCCESS)
                .statusText("签退成功")
                .data(attendanceInfoVO);
        }else {
            return new Response<>(ServiceStatus.BAD_REQUEST)
                    .statusText("没有可以签退的记录");
        }



    }



    @Operation(summary="查询社团成员当天的签到记录")
    @PostMapping("/getDayCheckIn")
    public Object getCheckInRecords(@RequestBody DayCheckInReq dayCheckInReq ) {
        // 调用服务层方法执行查询当天签到记录的逻辑
        List<AttendanceDO> dayCheckInRecords = attendanceService.getDayCheckInTest(dayCheckInReq);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(dayCheckInRecords);
    }



    @Operation(summary="负责人帮社团成员补签，时间格式为(2024-04-15 13:01:33)")
    @PostMapping("/applyAttendance")
    public Object makeUpAttendance(@RequestBody /* TODO jsr303 */ ApplyAttendanceReq applyAttendanceReq) {
        AttendanceInfoVO attendanceInfoVO = attendanceService.makeUpAttendance(applyAttendanceReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("补签成功")
                .data(attendanceInfoVO);
    }



    @Operation(summary="查询社团一个成员本周签到时长，返回秒")
    @GetMapping("/totalWeekSeconds")
    public Object getTotalWeekSeconds(
            @RequestParam("userId") String userId,
            @RequestParam("clubId") Long clubId){

        Long totalWeekSeconds = attendanceService.getTotalWeekSeconds(userId, clubId);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(totalWeekSeconds);
    }


    @Operation(summary="查询社团一个成员一个月的打卡时长，格式：年份（2024），月份（3），返回秒")
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

    @Operation(summary="查询社团一个成员一年打卡时长，格式：年份（2024），返回秒")
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





    @Operation(summary="查询社团一个成员指定时长打卡时长，返回秒，时间格式（2024-04-18 23:59:59）")
    @PostMapping("/anyTotalSecondsT")
    public Object getAnyTotalSecondsT(@RequestBody  GetOneAnyDurationReq getOneAnyDurationReq){

        Long totalAnySeconds = attendanceService.getAnyDurationSecondsT(getOneAnyDurationReq);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(totalAnySeconds);
    }



    @Operation(summary="查询社团每个成员每月的打卡时长，返回秒")
    @GetMapping("/eachTotalMonthSeconds")
    public Object getEachTotalMonthSeconds(
            @RequestParam("clubId") Long clubId,
            @RequestParam("year") int year,
            @RequestParam("month") int month){

        List<ClubAttendanceDurationVO> eachTotalMonthSeconds = attendanceService.getEachTotalMonthDuration(clubId, year,month);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(eachTotalMonthSeconds);
    }
    @Operation(summary="查询社团每个成员每年的打卡时长，返回秒")
    @GetMapping("/eachTotalYearSeconds")
    public Object getEachTotalYearSeconds(
            @RequestParam("clubId") Long clubId,
            @RequestParam("year") int year){

        List<ClubAttendanceDurationVO> eachTotalYearSeconds = attendanceService.getEachTotalYearDuration(clubId, year);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(eachTotalYearSeconds);
    }

    @Operation(summary="查询社团每个成员本周的打卡时长，返回秒")
    @GetMapping("/eachTotalWeekSeconds")
    public Object getEachTotalWeekSeconds(
            @RequestParam("clubId") Long clubId){

        List<ClubAttendanceDurationVO> eachTotalWeekSeconds = attendanceService.getEachTotalWeekDuration(clubId);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(eachTotalWeekSeconds);
    }




    @Operation(summary="查询社团每个成员指定时间段打卡时长，返回秒,时间格式（2024-04-18 23:59:59）")
    @PostMapping("/eachTotalAnySeconds")
    public Object getEachTotalAnySeconds(@RequestBody GetEachAnyDurationReq getEachAnyDurationReq){

        List<ClubAttendanceDurationVO> eachTotalAnySeconds = attendanceService
                .getEachTotalAnyDuration(getEachAnyDurationReq);

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(eachTotalAnySeconds);
    }
}
