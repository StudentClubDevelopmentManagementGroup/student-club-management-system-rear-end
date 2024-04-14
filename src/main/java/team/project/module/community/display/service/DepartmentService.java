package team.project.module.community.display.service;

import team.project.module.community.display.model.request.department.AddDepartmentReq;
import team.project.module.community.display.model.request.department.AlterDepartmentNameReq;
import team.project.module.community.display.model.view.DepartmentView;

import java.util.List;



public interface DepartmentService {
    //查询所有院系
    List<DepartmentView> getAllDepartments();

    //增加院系
    boolean addDepartment(AddDepartmentReq adddepartmentreq);

    //逻辑删除院系
    boolean deleteDepartmentByName(String name);
    //更改院系名
    boolean updateDepartment(AlterDepartmentNameReq alterDepartmentNameReq);
}
