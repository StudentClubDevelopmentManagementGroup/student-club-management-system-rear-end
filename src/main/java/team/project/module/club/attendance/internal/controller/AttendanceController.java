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
            @RequestParam("clubName") String clubName) {
        // 调用服务层方法执行查询当天签到记录的逻辑
        AttendanceInfoVO attendanceInfoVO = attendanceService.getLatestCheckInRecord(userId,clubName);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(attendanceInfoVO);
    }


    @Operation(summary="签到",description = """
            时间格式为(2024-04-15 13:01:33)
            """)
    @PostMapping("/checkInTest")
    public Object userCheckIn( @Valid @RequestBody  UserCheckInReq userCheckinReq) {

        AttendanceInfoVO attendanceInfoVO = attendanceService.userCheckIn(userCheckinReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("签到成功")
                .data(attendanceInfoVO);
    }



    @Operation(summary="签退",
            description = """
            时间格式为(2024-04-15 13:01:33)
            """)
    @PatchMapping ("/checkout")
    public Object userCheckoutTest(@Valid @RequestBody UserCheckoutReq userCheckoutReq) {

        AttendanceInfoVO attendanceInfoVO = attendanceService.userCheckOut(userCheckoutReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("签退成功")
                .data(attendanceInfoVO);

    }


    @Operation(summary="查时长，返回秒",
            description = """
                    - 时间格式（2024-04-18 23:59:59） \n
                    - 支持名字 或者 学号查询,支持模糊查询 \n
                    - userId和userName都为""查询社团全部成员的打卡时长 \n
                    - 开始结束时间都为 "" 则查询成员进入社团以来的全部打卡时长 \n
                    - 起止时间要么都为空，要么一起出现
                    """)
    @PostMapping("/durationTime")
    public Object getAttendanceTime(@Valid @RequestBody  GetAttendanceTimeReq getAttendanceTimeReq){
        List<ClubAttendanceDurationVO> eachAttendanceTime =
                attendanceService.getEachAttendanceDurationTime(getAttendanceTimeReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(eachAttendanceTime);
    }



    //查询社团成员指定时间段打卡记录
    @Operation(summary="查打卡记录",
            description = """
                    - 时间格式（2024-04-18 23:59:59） \n
                    - 支持名字 或者 学号查询,支持模糊查询 \n
                    - userId和userName都为"" 则查询社团全部成员的打卡记录 \n
                    - 开始结束时间都为 "" 则查询成员进入社团以来的全部打卡记录 \n
                    - 起止时间要么都为空，要么一起出现
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
                    .statusText("非法请求，签到时间与签退时间不在同一天");
        }

        AttendanceInfoVO attendanceInfoVO = attendanceService.userReplenishAttendance(applyAttendanceReq);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("补签成功")
                .data(attendanceInfoVO);

    }


}
