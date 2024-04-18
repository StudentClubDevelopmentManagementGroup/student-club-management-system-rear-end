package team.project.module.club.personnelchanges.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.personnelchanges.internal.model.request.ClubReq;
import team.project.module.club.personnelchanges.internal.model.request.UserClubReq;
import team.project.module.club.personnelchanges.internal.service.TblUserClubService;
@Tag(name="tbl_user_club_Controller")
@RestController
public class TblUserClubController {
    @Autowired
    TblUserClubService ucService;

    @Operation(summary="基地设置负责人")
    @PostMapping("/user_club/set_manager")
    Object setManager(@Valid @RequestBody UserClubReq req) {
        ucService.setClubManager(req.getUserId(),req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("设置成功");
    }


    @Operation(summary="基地撤销负责人")
    @PostMapping("/user_club/quash_manager")
    Object quashManager(@Valid @RequestBody UserClubReq req) {
        ucService.quashClubManager(req.getUserId(),req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("撤销成功");
    }

    @Operation(summary="添加基地成员")
    @PostMapping("/user_club/create_member")
    Object createMember(@Valid @RequestBody UserClubReq req) {
        ucService.createMember(req.getUserId(),req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("添加成功");
    }

    @Operation(summary="撤销基地成员")
    @PostMapping("/user_club/quash_member")
    Object quashMember(@Valid @RequestBody UserClubReq req) {
        ucService.quashMember(req.getUserId(),req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("撤销成功");
    }

    @Operation(summary="查询基地所有成员id")
    @PostMapping("/user_club/select_allMember")
    Object selectMember(@Valid @RequestBody ClubReq req) {

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(ucService.selectClubMember(req));

    }

    @Operation(summary="查询该用户是否是该社团成员")
    @PostMapping("/user_club/select_member")
    Object selecttheMember(@Valid @RequestBody UserClubReq req) {

        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(ucService.selectTheMember(req.getUserId(),req.getClubId()));

    }


}
