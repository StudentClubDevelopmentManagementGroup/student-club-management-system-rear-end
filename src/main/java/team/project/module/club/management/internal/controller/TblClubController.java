package team.project.module.club.management.internal.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.model.query.TblClubQO;
import team.project.module.club.management.internal.model.request.TblClubReq;
import team.project.module.club.management.internal.model.request.ClubReq;
import team.project.module.club.management.internal.service.TblClubService;

@Tag(name="tbl_club_Controller")
@RestController
public class TblClubController {
    @Autowired
    TblClubService service;


    @Operation(summary="创建基地")
    @PostMapping("/club/add")
    Object createClub (@Valid @RequestBody ClubReq req) {
        service.createClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("创建成功");
    }

    @Operation(summary="查询基地")
    @GetMapping("/club/select")
    Object selectClub(@Valid TblClubReq req){
        TblClubQO newQO = new TblClubQO(req.getDepartmentId(), req.getName(), req.getPagenum(), req.getSize());
        if(req.getName()==null){
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectByDepartmentId(newQO));
        }
        else if (req.getDepartmentId()==null){
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectByName(newQO));
        }
        else {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectByNameAndDepartmentId(newQO));
        }
    }

    @Operation(summary="删除基地")
    @PostMapping("/club/del")
    Object deleteClub(@Valid @RequestBody ClubReq req){
        service.deleteClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("删除成功");
    }

    @Operation(summary="撤销删除基地")
    @PostMapping("/club/undelete")
    Object recoverClub(@Valid @RequestBody ClubReq req) {
        service.recoverClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("撤销删除成功");
    }

    @Operation(summary="基地开放招人")
    @PostMapping("/club/recruitment/open")
    Object reuseClub(@Valid @RequestBody ClubReq req) {
        service.reuseClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }

    @Operation(summary="基地停止招人")
    @PostMapping("/club/recruitment/close")
    Object deactivateClub(@Valid @RequestBody ClubReq req) {
        service.deactivateClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }
    @Operation(summary="基地总信息")
    @GetMapping("/club/select_all")
    Object selectAll(@Valid TblClubReq req) {
        TblClubQO newQO = new TblClubQO(req.getDepartmentId(), req.getName(), req.getPagenum(), req.getSize());
        if (req.getDepartmentId() == null) {
            if (req.getName() == null) {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAll(newQO));
            } else {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAllByName(newQO));
            }
        } else {
            if (req.getName() == null) {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAllByDepartmentId(newQO));
            } else {
                return new Response<>(ServiceStatus.SUCCESS)
                        .statusText("查询成功")
                        .data(service.findAllByDepartmentIdAndName(newQO));
            }
        }
    }

}
