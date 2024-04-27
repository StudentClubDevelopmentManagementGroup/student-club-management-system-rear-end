package team.project.module.department.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.department.model.request.AddDepartmentReq;
import team.project.module.department.model.request.AlterDepartmentNameReq;
import team.project.module.department.service.DepartmentService;
import team.project.module.department.model.view.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Tag(name="院系管理")
@RestController
@Validated
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Operation(summary="查询所有院系以及校级部门")
    @GetMapping("/all")
    public Response<Object> getAllDepartments() {
        List<DepartmentVO> departments = departmentService.getAllDepartments();
        if (departments != null) {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(departments);
        } else {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data("院系表为空");
        }
    }


    @Operation(summary="根据id查询院系名称")
    @GetMapping("/name")
    public Response<Object> getDepartmentName( @RequestParam("departmentId") @NotNull Long id) {
        String departmentName = departmentService.getDepartmentName(id);
        if (departmentName != null) {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data(departmentName);
        } else {
            return new Response<>(ServiceStatus.SUCCESS)
                    .statusText("查询成功")
                    .data("没有该院系");
        }
    }

    @Operation(summary="逻辑删除院系，只需要传入院系全称")
    @DeleteMapping("/{name}")
    public Response<Object> deleteDepartmentByName(@PathVariable @NotNull String name) {
        boolean isDeleted = departmentService.deleteDepartmentByName(name);
        // 根据删除操作结果设置相应的提示信息
        String message = isDeleted ? "删除成功" : "删除失败";
        // 创建响应对象并设置状态码、状态文本和数据
        return new Response<>(isDeleted ? ServiceStatus.SUCCESS : ServiceStatus.INTERNAL_SERVER_ERROR)
                .statusText(message);
    }

    @Operation(summary="，修改院系全称，传入院系id修改full_name 字段")
    @PutMapping("/update")
    public Response<Object> updateDepartment(@RequestBody @Valid AlterDepartmentNameReq alterDepartmentNameReq) {
        boolean isUpdated = departmentService.updateDepartment(alterDepartmentNameReq);
        String message = isUpdated ? "修改成功" : "修改失败";
        return new Response<>(isUpdated ? ServiceStatus.SUCCESS : ServiceStatus.INTERNAL_SERVER_ERROR)
                .statusText(message);
    }

    @Operation(summary="添加院系，只需要abbreviation、full_name 字段")
    @PostMapping("/add")
    public Response<Object> addDepartment(@RequestBody @Valid AddDepartmentReq adddepartmentreq) {
        boolean isAdded = departmentService.addDepartment(adddepartmentreq);
        String message = isAdded ? "添加成功" : "添加失败";
        return new Response<>(isAdded ? ServiceStatus.SUCCESS : ServiceStatus.INTERNAL_SERVER_ERROR)
                .statusText(message);

    }
}
