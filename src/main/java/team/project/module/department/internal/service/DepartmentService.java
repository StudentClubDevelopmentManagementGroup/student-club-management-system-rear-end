package team.project.module.department.internal.service;

import team.project.module.department.internal.model.request.AddDepartmentReq;
import team.project.module.department.internal.model.request.AlterDepartmentNameReq;
import team.project.module.department.internal.model.view.DepartmentVO;

import java.util.List;



public interface DepartmentService {
    //查询所有院系
    List<DepartmentVO> getAllDepartments();

    //增加院系
    boolean addDepartment(AddDepartmentReq adddepartmentreq);

    //逻辑删除院系
    boolean deleteDepartmentByName(String name);
    //更改院系名
    boolean updateDepartment(AlterDepartmentNameReq alterDepartmentNameReq);

    //输入id获取院系名称
    String getDepartmentName(Long id);


}
