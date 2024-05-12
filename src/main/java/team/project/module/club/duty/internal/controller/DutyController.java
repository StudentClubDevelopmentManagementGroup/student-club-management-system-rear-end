package team.project.module.club.duty.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.duty.internal.model.entity.TblDuty;
import team.project.module.club.duty.internal.model.query.DutyInfoQO;
import team.project.module.club.duty.internal.model.request.*;
import team.project.module.club.duty.internal.service.DutyGroupService;
import team.project.module.club.duty.internal.service.DutyService;

@Tag(name = "值日")
@RestController
public class DutyController {
    @Autowired
    DutyGroupService dutyGroupService;

    @Autowired
    DutyService dutyService;

    @Operation(summary = "添加小组成员")
    @PostMapping("/club/duty/group/add")
    Object addDutyGroup(@Valid @RequestBody GroupMemberReq req) {
        dutyGroupService.createDutyGroup(req.getClub_id(), req.getMember_id(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary = "删除小组成员")
    @PostMapping("/club/duty/group/delete")
    Object deleteDutyGroup(@Valid @RequestBody GroupMemberReq req) {
        dutyGroupService.deleteDutyGroup(req.getClub_id(), req.getMember_id(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "根据小组名称以及社团id，添加值日信息")
    @PostMapping("/club/duty/create_by_group")
    Object createDutyByNameAndClubId(@Valid @RequestBody DutyInfoGroupReq req) {
        dutyService.createDutyByGroup(req.getNumber(), req.getArea(), req.getDuty_time(), req.getArranger_id(), req.getCleaner_id(), req.getClub_id(), req.getIs_mixed(), req.getGroup_name());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }


    @Operation(summary = "根据userid，添加值日信息")
    @PostMapping("/club/duty/create")
    Object createDutyByNameAndClubId(@Valid @RequestBody DutyInfoReq req) {
        dutyService.createDuty(req.getNumber(), req.getArea(), req.getDuty_time(), req.getArranger_id(), req.getCleaner_id(), req.getClub_id(), req.getIs_mixed());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary = "根据小组名称以及社团id，删除值日信息")
    @PostMapping("/club/duty/delete_by_group")
    Object deleteDutyByNameAndClubId(@Valid @RequestBody DutyInfoGroupReq req) {
        dutyService.deleteDutyAllByGroup(req.getDuty_time(), req.getGroup_name(), req.getClub_id());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "根据userid，删除值日信息")
    @PostMapping("/club/duty/delete")
    Object deleteDutyByNameAndClubId(@Valid @RequestBody DutyInfoReq req) {
        dutyService.deleteDutyByUser(req.getDuty_time(), req.getCleaner_id(), req.getClub_id());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "上传值日照片")
    @PostMapping("/club/duty/report_result")
    Object uploadDutyPicture(@Valid @RequestBody DutyFileUpload req) {
        dutyService.uploadDutyPicture(req.getDuty_time(), req.getMember_id(), req.getClub_id(), req.getFile());
        return new Response<>(ServiceStatus.SUCCESS).statusText("上传成功");
    }

    @Operation(summary = "查询社团值日情况")
    @PostMapping("/club/duty/select")
    Object selectDuty(@Valid @RequestBody DutySelectReq req) {

        DutyInfoQO newQO = new DutyInfoQO(req.getClub_id(), req.getNumber(), req.getName(), req.getPagenum(), req.getSize());
        PageVO<TblDuty> result = req.getName().isEmpty() ?
                (req.getNumber() == null || req.getNumber().isEmpty() ?
                        dutyService.selectDuty(newQO) :
                        dutyService.selectDutyByName(newQO)) :
                (req.getNumber() == null || req.getNumber().isEmpty() ?
                        dutyService.selectDutyByNumber(newQO) :
                        dutyService.selectDutyByNumberAndName(newQO));

        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }
}
