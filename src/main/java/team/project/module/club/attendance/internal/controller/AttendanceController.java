package team.project.module.club.attendance.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
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
    @PostMapping("/checkIn")
    public Object userCheckIn( @Valid @RequestBody  UserCheckInReq userCheckinReq) {
        // 查询当天最新的签到记录
        AttendanceInfoVO latestCheckInRecord = attendanceService.getLatestCheckInRecord(userCheckinReq.getUserId(), userCheckinReq.getClubId());

        // 如果最新签到记录存在且签退时间不为空，则表示上一次签到未签退
        if (latestCheckInRecord != null && latestCheckInRecord.getCheckoutTime() == null) {
            return new Response<>(ServiceStatus.BAD_REQUEST)
                    .statusText("签到失败，上一次签到未签退");
        }else {
            // 如果最新签到记录不存在或签退时间不为空，则可以进行签到操作
            AttendanceInfoVO attendanceInfoVO = attendanceService.userCheckIn(userCheckinReq);
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("签到成功")
                    .data(attendanceInfoVO);
        }
    }


    @Operation(summary="社团成员签退，时间格式为(2024-04-15 13:01:33)")
    @PatchMapping ("/checkout")
    public Object userCheckout(@Valid @RequestBody UserCheckoutReq userCheckoutReq) {
        AttendanceInfoVO attendanceInfoVO = attendanceService.userCheckOut(userCheckoutReq);
        if(attendanceInfoVO != null){
            return new Response<>(ServiceStatus.SUCCESS)
                .statusText("签退成功")
                .data(attendanceInfoVO);
        }else {
            return new Response<>(ServiceStatus.BAD_REQUEST)
                    .statusText("没有可以签退的记录");
        }

    }



    @Operation(summary="负责人帮社团成员补签，时间格式为(2024-04-15 13:01:33)")
    @PostMapping("/applyAttendance")
    public Object makeUpAttendance(@Valid @RequestBody  ApplyAttendanceReq applyAttendanceReq) {
        AttendanceInfoVO attendanceInfoVO = attendanceService.makeUpAttendance(applyAttendanceReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("补签成功")
                .data(attendanceInfoVO);
    }


    @Operation(summary="查询社团成员指定时间段打卡时长，返回秒，时间格式（2024-04-18 23:59:59）",
            description = """
                    -学号为空则查询社团全部成员的打卡时长 \n
                    -开始结束时间都为为空则查询成员进入社团以来的全部打卡时长
                    """)
    @PostMapping("/attendance/durationTime")
    public Object getAttendanceTime(@Valid @RequestBody  GetAttendanceTimeReq getAttendanceTimeReq){
        if(getAttendanceTimeReq.getUserId() ==null){
            List<ClubAttendanceDurationVO> eachAttendanceTime =
                    attendanceService.getEachAttendanceDurationTime(getAttendanceTimeReq);
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(eachAttendanceTime);
        }else {
            Long totalAnySeconds = attendanceService.getOneAttendanceDurationTime(getAttendanceTimeReq);
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(totalAnySeconds);
        }
    }


    //getEachAttendanceRecord
    @Operation(summary="查询社团成员指定时间段打卡记录，时间格式（2024-04-18 23:59:59）")
    @PostMapping("/attendance/record")
    public Object getEachAttendanceRecord(@Valid @RequestBody  GetAttendanceRecordReq getAttendanceRecordReq){
        if(getAttendanceRecordReq.getUserId() ==null){
            List<AttendanceInfoVO> eachAttendanceRecord =
                    attendanceService.getEachAttendanceRecord(getAttendanceRecordReq);

            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(eachAttendanceRecord);
        }else {
            List<AttendanceInfoVO> oneAttendanceRecord =
                    attendanceService.getEachAttendanceRecord(getAttendanceRecordReq);
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(oneAttendanceRecord);
        }
    }



}
