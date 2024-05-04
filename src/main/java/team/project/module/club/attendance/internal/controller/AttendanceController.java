package team.project.module.club.attendance.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.response.Response;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import team.project.module.club.attendance.internal.service.AttendanceService;

import java.time.LocalDateTime;
import java.util.List;



@Tag(name="签到签退模块")
@RestController
@RequestMapping("/attendance")
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



    @Operation(summary="签到",description = """
            时间格式为(2024-04-15 13:01:33)
            """)
    @PostMapping("/checkIn")
    public Object userCheckIn( @Valid @RequestBody  UserCheckInReq userCheckinReq) {
        LocalDateTime checkInTime = userCheckinReq.getCheckInTime();

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 校验签到时间是否为今天的日期且大于等于当前时间
        if (checkInTime.toLocalDate().isEqual(now.toLocalDate()) && checkInTime.isBefore(now)) {
            // 查询当天最新的签到记录
            AttendanceInfoVO latestCheckInRecord =
                    attendanceService.getLatestCheckInRecord(
                            userCheckinReq.getUserId(),
                            userCheckinReq.getClubId());

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

        }else {
            return new Response<>(ServiceStatus.BAD_REQUEST)
                    .statusText("签到失败")
                    .data("签到时间无效！");
        }

    }


    @Operation(summary="签退",
    description = """
            时间格式为(2024-04-15 13:01:33)
            """)
    @PatchMapping ("/checkout")
    public Object userCheckout(@Valid @RequestBody UserCheckoutReq userCheckoutReq) {
        //获取签退时间
        LocalDateTime checkoutTime = userCheckoutReq.getCheckoutTime();
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 校验签到时间是否为今天的日期且大于等于当前时间
        if (checkoutTime.toLocalDate().isEqual(now.toLocalDate()) && checkoutTime.isBefore(now)) {
            AttendanceInfoVO attendanceInfoVO = attendanceService.userCheckOut(userCheckoutReq);
            if(attendanceInfoVO != null){
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("签退成功")
                        .data(attendanceInfoVO);
            }else {
                return new Response<>(ServiceStatus.BAD_REQUEST)
                        .statusText("没有可以签退的记录");
            }

        }else {
            return new Response<>(ServiceStatus.BAD_REQUEST)
                    .statusText("签退失败")
                    .data("签退时间无效！");
        }


    }

    @Operation(summary="查时长，返回秒",
            description = """
                    时间格式（2024-04-18 23:59:59）\n
                    查询社团全部人的打卡时长没有userId参数，删掉即可\n
                    -开始结束时间都为 "" 则查询成员进入社团以来的全部打卡时长
                    """)
    @PostMapping("/durationTime")
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

    //查询社团成员指定时间段打卡记录
    @Operation(summary="查打卡记录",
            description = """
                    时间格式（2024-04-18 23:59:59）\n
                    -学号为 "" 则查询社团全部成员的打卡记录 \n
                    -开始结束时间都为 "" 则查询成员进入社团以来的全部打卡记录
                    """)
    @PostMapping("/record")
    public Object getAttendanceRecord(@Valid @RequestBody  GetAttendanceRecordReq getAttendanceRecordReq){
        PageVO<AttendanceInfoVO> eachAttendanceRecord =
                //getAttendanceRecordT
                attendanceService.getAttendanceRecord(getAttendanceRecordReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(eachAttendanceRecord);
    }



    @Operation(summary="补签",
            description = """
                    社团成员申请补签，超过签到时间七天请求无效
                    """)
    @PostMapping("/replenish")
    public Object replenishAttendanceRecord(@Valid @RequestBody  ApplyAttendanceReq applyAttendanceReq){
        if(!applyAttendanceReq.getCheckInTime().toLocalDate()
                .equals(applyAttendanceReq.getCheckoutTime().toLocalDate())) {
            return new Response<>(ServiceStatus.BAD_REQUEST)
                    .statusText("非法请求，时间无效");
        }

        AttendanceInfoVO attendanceInfoVO = attendanceService.userReplenishAttendance(applyAttendanceReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("补签成功")
                .data(attendanceInfoVO);

    }


}
