package team.project.module.manage_lin.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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
        List<tbl_club_DO> result = service.create_club(department_id, name);
        if(result!=null)
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("创建成功")
                    .data(result);
        else
            return new Response<>(ServiceStatus.FAILED_DEPENDENCY)
                    .statusText("创建失败")
                    .data(result);
    }

    @Operation(summary="查询基地")
    @GetMapping("/manage_all/select_clb/{name}")
    Object select_clb(@PathVariable String name){
        List<tbl_club_DO> result = service.findbyname(name);
        if (result!=null)
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(service.findbyname(name));
        else
            return new Response<>(ServiceStatus.FAILED_DEPENDENCY)
                    .statusText("查询失败")
                    .data(service.findbyname(name));
    }

}
