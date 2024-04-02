package team.project.module.club.management.internal.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.request.TblClubReq;
import team.project.module.club.management.internal.model.view.ClubManagerVO;
import team.project.module.club.management.internal.model.view.ClubMasVO;
import team.project.module.club.management.internal.model.view.ClubVO;
import team.project.module.club.management.internal.service.TblClubService;
import team.project.module.club.management.internal.service.TblUserClubService;

@Tag(name="tbl_club_Controller")
@RestController
public class TblClubController {
    @Autowired
    TblClubService service;
    @Autowired
    TblUserClubService service2;

    @Operation(summary="创建基地")
    @PostMapping("/manage_all/create_club")
    Object createClub (@RequestBody ClubVO req) {
        service.createClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("创建成功");
    }

    @Operation(summary="查询基地")
    @GetMapping("/manage_all/select_club")
    Object selectClub(TblClubReq req){
        if(req.getName()==null){
            Page<TblClubDO> page = new Page<>(req.getPagenum(), req.getSize());
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectByDepartmentId(page,req.getDepartmentId()));
        }
        else if (req.getDepartmentId()==null){
            Page<TblClubDO> page = new Page<>(req.getPagenum(), req.getSize());
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectByName(page,req.getName()));
        }
        else {
            Page<TblClubDO> page = new Page<>(req.getPagenum(), req.getSize());
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectByNameAndDepartmentId(page,req.getDepartmentId(), req.getName()));
        }
    }

    @Operation(summary="删除基地")
    @PostMapping("/manage_all/delete_club")
    Object deleteClub(@RequestBody ClubVO req){
        service.deleteClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("删除成功");
    }

    @Operation(summary="撤销删除基地")
    @PostMapping("/manage_all/recover_club")
    Object recoverClub(@RequestBody ClubVO req) {
        service.recoverClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("撤销删除成功");
    }

    @Operation(summary="基地开放招人")
    @PostMapping("/manage_all/reuse_club")
    Object reuseClub(@RequestBody ClubVO req) {
        service.reuseClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }

    @Operation(summary="基地停止招人")
    @PostMapping("/manage_all/deactivate_club")
    Object deactivateClub(@RequestBody ClubVO req) {
        service.deactivateClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }
    @Operation(summary="基地总信息")
    @GetMapping("/manage_all/select_all")
    Object selectAll(TblClubReq req) {
        if (req.getDepartmentId() == null) {
            if (req.getName() == null) {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAll(req));
            } else {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAllByName(req));
            }
        } else {
            if (req.getName() == null) {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAllByDepartmentId(req));
            } else {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAllByDepartmentIdAndName(req));
            }
        }
    }
    @Operation(summary="基地设置负责人")
    @PostMapping("/manage_all/select_manager")
    Object selectManager(@RequestBody ClubManagerVO req) {
        service2.setClubManager(req.getUserId(),req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("设置成功");
    }


    @Operation(summary="基地撤销负责人")
    @PostMapping("/manage_all/quash_manager")
    Object quashManager(@RequestBody ClubManagerVO req) {
        service2.quashClubManager(req.getUserId(),req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("撤销成功");
    }
}
