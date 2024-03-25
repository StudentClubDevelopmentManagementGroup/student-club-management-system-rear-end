package team.project.module.community.display.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.community.display.service.DepartmentService;
import team.project.module.community.display.entity.Club;
import team.project.module.community.display.entity.Department;
import team.project.module.community.display.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Tag(name="tbl_department")
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    private ClubService clubService;

    public DepartmentController(DepartmentService departmentService ,ClubService clubService) {
        this.departmentService = departmentService;
        this.clubService=clubService;
    }
    @Operation(summary="查询所有院系以及校级部门")
    @GetMapping("all")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @Operation(summary="根据院系ID查询院系的社团")
    @PostMapping("/clubs")
    public List<Club> getClubsByDepartmentId(
            @RequestBody @Parameter(description = "请输入部门ID", required = true) Long departmentId) {
        return clubService.selectClubsByDepartment( departmentId);
    }



    @Operation(summary="添加院系，只需要abbreviation、full_name 字段")
    @PostMapping("/add")
    public Response<Object> addDepartment(@RequestBody Department department) {
        boolean isAdded = departmentService.addDepartment(department);

        // 根据添加操作结果设置相应的提示信息
        String message = isAdded ? "添加成功" : "添加失败";

        // 创建响应对象并设置状态码、状态文本和数据
        return new Response<>(isAdded ? ServiceStatus.SUCCESS : ServiceStatus.INTERNAL_SERVER_ERROR)
                .data(message);
    }

    @Operation(summary="逻辑删除院系，只需要传入院系全称")
    @DeleteMapping("/{name}")
    public Response<Object> deleteDepartmentByName(@PathVariable String name) {
        boolean isDeleted = departmentService.deleteDepartmentByName(name);

        // 根据删除操作结果设置相应的提示信息
        String message = isDeleted ? "删除成功" : "删除失败";

        // 创建响应对象并设置状态码、状态文本和数据
        return new Response<>(isDeleted ? ServiceStatus.SUCCESS : ServiceStatus.INTERNAL_SERVER_ERROR)
                .data(message);
    }

    @Operation(summary="传入简称修改院系全称，只需要abbreviation、full_name 字段")
    @PutMapping("/update")
    public Response<Object> updateDepartment(@RequestBody Department department) {
        boolean isUpdated = departmentService.updateDepartment(department);

        // 根据更新操作结果设置相应的提示信息
        String message = isUpdated ? "修改成功" : "修改失败";

        // 创建响应对象并设置状态码、状态文本和数据
        return new Response<>(isUpdated ? ServiceStatus.SUCCESS : ServiceStatus.INTERNAL_SERVER_ERROR)
                .data(message);
    }

}
