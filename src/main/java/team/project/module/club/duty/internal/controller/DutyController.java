package team.project.module.club.duty.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.controller.response.Response;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.duty.internal.model.entity.TblDutyCirculation;
import team.project.module.club.duty.internal.model.query.DutyGroupQO;
import team.project.module.club.duty.internal.model.query.DutyInfoQO;
import team.project.module.club.duty.internal.model.query.DutyInfoSelfQO;
import team.project.module.club.duty.internal.model.request.*;
import team.project.module.club.duty.internal.model.view.DutyGroupSelectVO;
import team.project.module.club.duty.internal.model.view.DutyInfoVO;
import team.project.module.club.duty.internal.service.DutyCirculationService;
import team.project.module.club.duty.internal.service.DutyGroupService;
import team.project.module.club.duty.internal.service.DutyService;

import java.time.LocalDateTime;

@Tag(name = "值日")
@RestController
public class DutyController {
    @Autowired
    DutyGroupService dutyGroupService;

    @Autowired
    DutyService dutyService;

    @Autowired
    DutyCirculationService dutyCirculationService;

    @Autowired
    AuthServiceI authService;

    @Operation(summary = "添加小组成员")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/duty/group/add")
    Object addDutyGroup(@Valid @RequestBody GroupMemberReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能添加小组成员");

        dutyGroupService.createDutyGroup(req.getClubId(), req.getMemberId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary = "删除小组成员")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/duty/group/delete")
    Object deleteDutyGroup(@Valid @RequestBody GroupMemberReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能删除小组成员");

        dutyGroupService.deleteDutyGroup(req.getClubId(), req.getMemberId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "根据小组名称以及社团id，添加值日信息")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/duty/create_by_group")
    Object createDutyByNameAndClubId(@Valid @RequestBody DutyInfoGroupReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能添加值日信息");

        dutyService.createDutyByGroup(req.getNumber(), req.getArea(), req.getDateTime(), req.getArrangerId(), req.getClubId(), req.getIsMixed(), req.getGroupName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }


    @Operation(summary = "根据userid，添加值日信息")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/duty/create")
    Object createDutyByNameAndClubId(@Valid @RequestBody DutyInfoReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能添加值日信息");

        dutyService.createDuty(req.getNumber(), req.getArea(), req.getDateTime(), req.getArrangerId(), req.getCleanerId(), req.getClubId(), req.getIsMixed());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary = "根据小组名称以及社团id，删除值日信息")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/duty/delete_by_group")
    Object deleteDutyByNameAndClubId(@Valid @RequestBody DutyDeleteGroupReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能删除值日信息");

        dutyService.deleteDutyAllByGroup(req.getDateTime(), req.getGroupName(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "根据userid，删除值日信息")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/duty/delete")
    Object deleteDutyByNameAndClubId(@Valid @RequestBody DutyDeleteReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能删除值日信息");

        dutyService.deleteDutyByUser(req.getDateTime(), req.getCleanerId(), req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary = "上传多张值日照片")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/duty/report_results")
    Object uploadDutyPicture2(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam("date_time")    LocalDateTime         dateTime,
            @RequestParam("member_id")    String                memberId,
            @RequestParam("club_id")      Long                  clubId,
            @RequestPart("file")          MultipartFile[]         file)
    {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubMember(arrangerId, clubId, "只有社团成员能上传值日照片");
        return new Response<>(ServiceStatus.SUCCESS).statusText("上传成功").data(dutyService.uploadDutyPictures(dateTime, memberId, clubId, file));
    }

    @Operation(summary = "查询社团值日情况")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/duty/select")
    Object selectDuty(@Valid @RequestBody DutySelectReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubMember(arrangerId, req.getClubId(), "只有社团成员能查询社团值日情况");
        DutyInfoQO newQO = new DutyInfoQO(req.getClubId(), req.getNumber(), req.getName(), req.getPageNum(), req.getSize());
        PageVO<DutyInfoVO> result = req.getName().isEmpty() ?
                (req.getNumber().isBlank() || req.getNumber().isEmpty() ?
                        dutyService.selectDuty(newQO) :
                        dutyService.selectDutyByNumber(newQO)):
                (req.getNumber().isBlank() || req.getNumber().isEmpty() ?
                        dutyService.selectDutyByName(newQO):
                        dutyService.selectDutyByNumberAndName(newQO));
        //todo 合并
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "查询社团值日小组")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/duty/group/select")
    Object selectDutyGroup(@Valid @RequestBody DutyGroupSelectReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubMember(arrangerId, req.getClubId(), "只有社团成员能查询社团值日小组");

        DutyGroupQO newQO = new DutyGroupQO(req.getClubId(), req.getGroupName(), req.getName(), req.getPageNum(), req.getSize());
        PageVO<DutyGroupSelectVO> result = req.getName().isEmpty() ?
                (req.getGroupName().isBlank() || req.getGroupName().isEmpty() ?
                        dutyGroupService.selectDutyGroup(newQO) :
                        dutyGroupService.selectDutyGroupByGroupName(newQO)) :
                (req.getGroupName().isBlank() || req.getGroupName().isEmpty() ?
                        dutyGroupService.selectDutyGroupByName(newQO) :
                        dutyGroupService.selectDutyGroupByGroupNameAndName(newQO));
        //todo 合并
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "查询是否开启自动安排值日功能")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/duty/auto_duty")
    Object selectAutoDuty(@Valid @NotNull @RequestParam @JsonProperty("club_id") Long clubId) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubMember(arrangerId, clubId, "只有社团成员能是否开启自动安排值日功能");

        TblDutyCirculation result = dutyCirculationService.selectCirculationByClubId(clubId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "查询自己值日的情况")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/duty/selectself")
    Object selectDutySelf(@Valid @RequestBody DutySelectSelfReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubMember(arrangerId, req.getClubId(), "只有社团成员能查询自己值日情况");

        PageVO<DutyInfoVO> result = dutyService.selectDutyByUserId(new DutyInfoSelfQO(req.getClubId(), req.getPageNum(), req.getSize()), arrangerId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary = "设置自动安排值日功能，0是开启，1是关闭")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/duty/auto_duty/set")
    Object openAutoDuty(@Valid @RequestBody DutyCirculationReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(arrangerId, req.getClubId(), "只有社团负责人能开启自动安排值日功能");

        dutyCirculationService.setCirculationByClubId(req.getClubId(), req.getCirculation());
        return new Response<>(ServiceStatus.SUCCESS).statusText("设置成功");
   }
}
