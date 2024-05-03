package team.project.module.club.duty.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.duty.internal.model.request.DutyInfoReq;
import team.project.module.club.duty.internal.model.request.GroupMemberReq;
import team.project.module.club.duty.internal.service.DutyGroupService;
import team.project.module.club.duty.internal.service.DutyService;

@Tag(name="值日")
@RestController
public class DutyController {
    @Autowired
    DutyGroupService dutyGroupService;

    @Autowired
    DutyService dutyService;
    @Operation(summary="添加小组成员")
    @PostMapping("/club/duty/group/create")
    Object createDutyGroup (@Valid @RequestBody GroupMemberReq req) {
        dutyGroupService.createDutyGroup( req.getClub_id(),req.getMember_id(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary="删除小组成员")
    @PostMapping("/club/duty/group/delete")
    Object deleteDutyGroup (@Valid @RequestBody GroupMemberReq req) {
        dutyGroupService.deleteDutyGroup( req.getClub_id(),req.getMember_id(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary="根据小组名称以及社团id，添加值日信息")
    @PostMapping("/club/duty/createbynameandclub_id")
    Object createDutyByNameAndClubId (@Valid @RequestBody DutyInfoReq req) {
        dutyService.createDuty( req.getNumber(), req.getArea(), req.getDuty_time()
                , req.getArranger_id(),req.getCleaner_id(),req.getClub_id(), req.getIsmixed());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }
}
