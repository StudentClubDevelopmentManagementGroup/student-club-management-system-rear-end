package team.project.module.community_display.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import team.project.module.community_display.entity.Club;
import team.project.module.community_display.entity.Department;
import team.project.module.community_display.service.ClubService;
import team.project.module.community_display.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Tag(name="tbl_department")
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private  DepartmentService departmentService;
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
}
