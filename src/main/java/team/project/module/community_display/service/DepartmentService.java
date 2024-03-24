package team.project.module.community_display.service;

import team.project.module.community_display.entity.Club;
import team.project.module.community_display.entity.Department;
import java.util.List;

public interface DepartmentService {
    //查询所有院系
    List<Department> getAllDepartments();
    //增加院系
    boolean addDepartment(Department department);
    //逻辑删除院系
    boolean deleteDepartmentByName(String name);
    //更改院系名
    boolean updateDepartment(Department department);
}
