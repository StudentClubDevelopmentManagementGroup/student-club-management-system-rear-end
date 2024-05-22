package team.project.module.club.duty.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.duty.internal.model.entity.TblDutyCirculation;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;
import team.project.module.club.duty.internal.model.query.DutyGroupQO;
import team.project.module.club.duty.internal.model.query.DutyInfoQO;
import team.project.module.club.duty.internal.model.request.*;
import team.project.module.club.duty.internal.model.view.DutyInfoVO;
import team.project.module.club.duty.internal.service.DutyCirculationService;
import team.project.module.club.duty.internal.service.DutyGroupService;
import team.project.module.club.duty.internal.service.DutyService;

@Tag(name = "值日")
@RestController
public class DutyController {
    @Autowired
    DutyGroupService dutyGroupService;

    @Autowired
    DutyService dutyService;

    @Autowired
    DutyCirculationService dutyCirculationService;

    @Operation(summary = "添加小组成员")
    @PostMapping("/club/duty/group/add")
    Object addDutyGroup(@Valid @RequestBody GroupMemberReq req) {
        dutyGroupService.createDutyGroup(req.getClubId(), req.getMemberId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary = "删除小组成员")
    @PostMapping("/club/duty/group/delete")
    Object deleteDutyGroup(@Valid @RequestBody GroupMemberReq req) {
        dutyGroupService.deleteDutyGroup(req.getClubId(), req.getMemberId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "根据小组名称以及社团id，添加值日信息")
    @PostMapping("/club/duty/create_by_group")
    Object createDutyByNameAndClubId(@Valid @RequestBody DutyInfoGroupReq req) {
        dutyService.createDutyByGroup(req.getNumber(), req.getArea(), req.getDateTime(), req.getArrangerId(), req.getCleanerId(), req.getClubId(), req.getIsMixed(), req.getGroupName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }


    @Operation(summary = "根据userid，添加值日信息")
    @PostMapping("/club/duty/create")
    Object createDutyByNameAndClubId(@Valid @RequestBody DutyInfoReq req) {
        dutyService.createDuty(req.getNumber(), req.getArea(), req.getDateTime(), req.getArrangerId(), req.getCleanerId(), req.getClubId(), req.getIsMixed());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary = "根据小组名称以及社团id，删除值日信息")
    @PostMapping("/club/duty/delete_by_group")
    Object deleteDutyByNameAndClubId(@Valid @RequestBody DutyDeleteGroupReq req) {
        dutyService.deleteDutyAllByGroup(req.getDateTime(), req.getGroupName(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "根据userid，删除值日信息")
    @PostMapping("/club/duty/delete")
    Object deleteDutyByNameAndClubId(@Valid @RequestBody DutyDeleteReq req) {
        dutyService.deleteDutyByUser(req.getDateTime(), req.getCleanerId(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "上传值日照片")
    @PostMapping("/club/duty/report_result")
    Object uploadDutyPicture(@Valid @RequestBody DutyFileUpload req) {
        dutyService.uploadDutyPicture(req.getDateTime(), req.getMemberId(), req.getClubId(), req.getFile());
        return new Response<>(ServiceStatus.SUCCESS).statusText("上传成功");
    }

    @Operation(summary = "查询社团值日情况")
    @PostMapping("/club/duty/select")
    Object selectDuty(@Valid @RequestBody DutySelectReq req) {

        DutyInfoQO newQO = new DutyInfoQO(req.getClubId(), req.getNumber(), req.getName(), req.getPageNum(), req.getSize());
        PageVO<DutyInfoVO> result = req.getName().isEmpty() ?
                (req.getNumber() == null || req.getNumber().isEmpty() ?
                        dutyService.selectDuty(newQO) :
                        dutyService.selectDutyByName(newQO)) :
                (req.getNumber() == null || req.getNumber().isEmpty() ?
                        dutyService.selectDutyByNumber(newQO) :
                        dutyService.selectDutyByNumberAndName(newQO));
        //todo 合并
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "查询社团值日小组")
    @PostMapping("/club/duty/group/select")
    Object selectDutyGroup(@Valid @RequestBody DutyGroupSelectReq req) {
        DutyGroupQO newQO = new DutyGroupQO(req.getClubId(), req.getGroupName(), req.getName(), req.getPageNum(), req.getSize());
        PageVO<TblDutyGroup> result = req.getName().isEmpty() ?
                (req.getGroupName() == null || req.getGroupName().isEmpty() ?
                        dutyGroupService.selectDutyGroup(newQO) :
                        dutyGroupService.selectDutyGroupByName(newQO)) :
                (req.getGroupName() == null || req.getGroupName().isEmpty() ?
                        dutyGroupService.selectDutyGroupByGroupName(newQO) :
                        dutyGroupService.selectDutyGroupByGroupNameAndName(newQO));
        //todo 合并
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "查询自动值日")
    @PostMapping("/club/duty/auto_duty")
    Object selectAutoDuty(@Valid @NotNull @RequestBody Long clubId) {
        TblDutyCirculation result = dutyCirculationService.selectCirculationByClubId(clubId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }
}
