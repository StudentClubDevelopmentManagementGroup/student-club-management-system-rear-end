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
import team.project.module.club.management.internal.model.view.ClubMasVO;
import team.project.module.club.management.internal.service.TblClubService;

@Tag(name="tbl_club_Controller")
@RestController
public class TblClubController {
    @Autowired
    TblClubService service;

    @Operation(summary="创建基地")
    @PostMapping("/manage_all/create_club")
    Object createClub (@RequestParam("department_id") Long departmentId, String name) {
        service.createClub(departmentId, name);
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
    Object deleteClub(@RequestParam("department_id") Long departmentId, String name){
        service.deleteClub(departmentId,name);
        return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("删除成功");
    }

    @Operation(summary="撤销删除基地")
    @PostMapping("/manage_all/recover_club")
    Object recoverClub(@RequestParam("department_id") Long departmentId,  String name) {
        service.recoverClub(departmentId, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("撤销删除成功");
    }

    @Operation(summary="基地开放招人")
    @PostMapping("/manage_all/reuse_club")
    Object reuseClub(@RequestParam("department_id") Long departmentId,  String name) {
        service.reuseClub(departmentId, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }


    @Operation(summary="基地停止招人")
    @PostMapping("/manage_all/deactivate_clb")
    Object deactivateClb(Long departmentId, String name) {
        service.deactivateClub(departmentId, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }


    @Operation(summary="首页查询")
    @GetMapping("/manage_all/select_all")
    Object selectAll(ClubMasVO req){
        Page<ClubMasVO> page = new Page<>(req.getPagenum(), req.getSize());
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("查询成功")
                .data(service.findAll(page));
    }
}
