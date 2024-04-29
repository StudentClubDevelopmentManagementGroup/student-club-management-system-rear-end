package team.project.module.club.management.internal.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.response.Response;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.query.ClubInfoQO;
import team.project.module.club.management.internal.model.request.ListClubInfoReq;
import team.project.module.club.management.internal.model.request.OneClubInfoReq;
import team.project.module.club.management.internal.service.TblClubService;

import java.util.Objects;

@Tag(name="社团管理")
@RestController
public class TblClubController {

    @Autowired
    TblClubService service;

    @Operation(summary="创建基地")
    @PostMapping("/club/add")
    Object createClub (@Valid @RequestBody OneClubInfoReq req) {
        service.createClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }

    @Operation(summary="查询基地基础信息")
    @GetMapping("/club/select")
    Object selectClub(@Valid ListClubInfoReq req){
        ClubInfoQO newQO = new ClubInfoQO(req.getDepartmentId(), req.getName(), req.getPagenum(), req.getSize());

        PageVO<TblClubDO> result;
        if (Objects.equals(req.getName(), "")) {
            result = service.selectByDepartmentId(newQO);
        }
        else if (req.getDepartmentId() == 0) {
            result = service.selectByName(newQO);
        }
        else {
            result = service.selectByNameAndDepartmentId(newQO);
        }

        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }

    @Operation(summary="删除基地")
    @PostMapping("/club/del")
    Object deleteClub(@Valid @RequestBody OneClubInfoReq req){
        service.deleteClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary="撤销删除基地")
    @PostMapping("/club/undelete")
    Object recoverClub(@Valid @RequestBody OneClubInfoReq req) {
        service.recoverClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("撤销删除成功");
    }

    @Operation(summary="基地开放招人")
    @PostMapping("/club/recruitment/open")
    Object reuseClub(@Valid @RequestBody OneClubInfoReq req) {
        service.reuseClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("修改成功");
    }

    @Operation(summary="基地停止招人")
    @PostMapping("/club/recruitment/close")
    Object deactivateClub(@Valid @RequestBody OneClubInfoReq req) {
        service.deactivateClub(req.getDepartmentId(), req.getName());
        return new Response<>(ServiceStatus.SUCCESS).statusText("修改成功");
    }
    @Operation(summary="基地总信息，包括人数、负责人以及是否开放招新", description="""
    4564
    654654
    4654654
    """)  // TODO 补充详细的api介绍
    @GetMapping("/club/select_all")
    Object selectAll(@Valid ListClubInfoReq req) {

        ClubInfoQO newQO = new ClubInfoQO(req.getDepartmentId(), req.getName(), req.getPagenum(), req.getSize());
        PageVO<ClubMsgDTO> result;

        if (req.getDepartmentId() == 0) {
            if (req.getName() == null || "".equals(req.getName())) {
                result = service.findAll(newQO);
            } else {
                result = service.findAllByName(newQO);
            }
        }
        else {
            if (req.getName() == null || "".equals(req.getName())) {
                result = service.findAllByDepartmentId(newQO);
            } else {
                result = service.findAllByDepartmentIdAndName(newQO);
            }
        }

        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(result);
    }
}
