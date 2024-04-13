package team.project.module.community.display.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.community.display.model.entity.Club;
import team.project.module.community.display.model.request.UserCheckinReq;
import team.project.module.community.display.model.view.DepartmentView;
import team.project.module.community.display.service.AttendenceService;

import java.util.List;

@Tag(name="签到签退模块")
@RestController
public class AttendenceController {

    private final AttendenceService attendenceService;

    @Autowired
    public AttendenceController(AttendenceService attendenceService) {
        this.attendenceService = attendenceService;
    }


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
}
