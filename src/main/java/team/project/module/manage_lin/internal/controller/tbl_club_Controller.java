package team.project.module.manage_lin.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;
import team.project.module.manage_lin.internal.service.tbl_club_Service;

import java.util.List;

@Tag(name="tbl_club_Controller")
@RestController
public class tbl_club_Controller {
    @Autowired
    tbl_club_Service service;

    @Operation(summary="创建基地")
    @PostMapping("/manage_all/create_clb")
    Object create_clb (Long department_id, String name) {
        service.create_club(department_id, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("创建成功");
    }

    @Operation(summary="查询基地")
    @GetMapping("/manage_all/select_club")
    Object select_clb(Long department_id, String name){
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.findbynameBetweendepartmentId(department_id,name));
    }

    @Operation(summary="删除基地")
    @PostMapping("/manage_all/delete_clb")
    Object delete_clb(@RequestParam("department_id") Long departmentId, String name){
        service.delete_club(departmentId,name);
        return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("删除成功");
    }

    @Operation(summary="基地开放招人")
    @PostMapping("/manage_all/update_clb")
    Object update_clb(Long department_id, String name) {
        service.reuse_club(department_id, name);
        return new Response<>(ServiceStatus.SUCCESS)
                .statusText("修改成功");
    }

}
