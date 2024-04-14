package team.project.module.community.display.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.community.display.model.entity.Attendence;
import team.project.module.community.display.model.request.attendence.DayCheckInReq;
import team.project.module.community.display.model.request.attendence.UserCheckinReq;
import team.project.module.community.display.service.AttendenceService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name="签到签退模块")
@RestController
@RequestMapping("/clockIn")
public class AttendenceController {

    @Autowired //注入
    private  AttendenceService attendenceService;


    @Operation(summary="社团成员签到")
    @PostMapping("/checkin")
    public Response<Object> userCheckIn(@RequestBody UserCheckinReq userCheckinReq) {
        boolean success = attendenceService.userCheckIn(userCheckinReq);
        if (success) {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("签到成功")
                    .data("签到成功");
        } else {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR)
                    .statusText("签到失败")
                    .data("签到失败");
        }
    }

    @Operation(summary="查询社团成员当天的签到记录")
    @PostMapping("/getDayCheckIn")
    public Response<Object> getCheckinRecords(@RequestBody DayCheckInReq dayCheckInReq ) {
        // 调用服务层方法执行查询当天签到记录的逻辑
        List<Attendence> dayCheckInRecords = attendenceService.getDayCheckIn(dayCheckInReq);
        if(!dayCheckInRecords.isEmpty()){
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(dayCheckInRecords);
        }else {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR)
                    .statusText("查询失败")
                    .data("查询失败");
        }

    }
}
