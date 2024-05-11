package team.project.module.department.internal.service.impl;

import team.project.module.department.internal.mapper.DepartmentMapper;
import team.project.module.department.internal.model.entity.Department;
import team.project.module.department.internal.model.request.AddDepartmentReq;
import team.project.module.department.internal.model.request.AlterDepartmentNameReq;
import team.project.module.department.internal.model.view.DepartmentVO;
import team.project.module.department.internal.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class DepartmentServiceImpl implements DepartmentService {


    @Autowired
    private DepartmentMapper departmentMapper;


    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;

    }

    @Override
    public List<DepartmentVO> getAllDepartments() {
        return departmentMapper.selectAll();
    }

    @Override
    public boolean addDepartment(AddDepartmentReq adddepartmentreq) {
        int rowsAffected = departmentMapper.addDepartment(adddepartmentreq);
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteDepartmentByName(String name) {
        int rowsAffected = departmentMapper.deleteDepartmentByName(name);
        return rowsAffected > 0;
    }

    @Override
    public boolean updateDepartment(AlterDepartmentNameReq alterDepartmentNameReq) {
        int rowsAffected = departmentMapper.updateDepartmentName(alterDepartmentNameReq);
        return rowsAffected > 0;
    }

    @Override
    //输入id获取院系名称
    public String getDepartmentName(Long id){
        return departmentMapper.getDepartmentName(id);
    }






}
