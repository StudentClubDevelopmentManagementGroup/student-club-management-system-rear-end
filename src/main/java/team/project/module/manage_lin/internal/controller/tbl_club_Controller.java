package team.project.module.manage_lin.internal.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;
import team.project.module.manage_lin.internal.model.request.tbl_club_Req;
import team.project.module.manage_lin.internal.service.tbl_club_Service;

import java.util.List;

@Tag(name="tbl_club_Controller")
@RestController
public class tbl_club_Controller {
    @Autowired
    tbl_club_Service service;

    tbl_club_Req req;
    @Operation(summary="创建基地")
    @PostMapping("/manage_all/create_clb")
    Object create_clb (@RequestParam("department_id") Long departmentId, String name) {
        service.create_club(departmentId, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("创建成功");
    }

    @Operation(summary="查询基地")
    @GetMapping("/manage_all/select_club")
    Object select_club(tbl_club_Req req){
        if(req.getName()==null){
            Page<tbl_club_DO> page = new Page<>(req.getPagenum(), req.getSize());
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectPageBydepartmentId(page,req.getDepartmentId()));
        }
        else if (req.getDepartmentId()==null){
            Page<tbl_club_DO> page = new Page<>(req.getPagenum(), req.getSize());
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectPageByname(page,req.getName()));
        }
        else {
            Page<tbl_club_DO> page = new Page<>(req.getPagenum(), req.getSize());
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.selectPageBynamebetweendepartmentId(page,req.getDepartmentId(), req.getName()));
        }
    }

    @Operation(summary="删除基地")
    @PostMapping("/manage_all/delete_clb")
    Object delete_clb(@RequestParam("department_id") Long departmentId, String name){
        service.delete_club(departmentId,name);
        return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("删除成功");
    }

    @Operation(summary="重启基地")
    @PostMapping("/manage_all/recover_clb")
    Object recover_clb(@RequestParam("department_id") Long departmentId,  String name) {
        service.recover_club(departmentId, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }

    @Operation(summary="基地开放招人")
    @PostMapping("/manage_all/reuse_club")
    Object reuse_club(@RequestParam("department_id") Long departmentId,  String name) {
        service.reuse_club(departmentId, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }


    @Operation(summary="基地停止招人")
    @PostMapping("/manage_all/deactivate_clb")
    Object deactivate_clb(Long department_id, String name) {
        service.deactivate_clb(department_id, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }

}
