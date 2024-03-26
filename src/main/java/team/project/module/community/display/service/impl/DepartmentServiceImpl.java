package team.project.module.community.display.service.impl;

import team.project.module.community.display.model.entity.Department;
import team.project.module.community.display.mapper.DepartmentMapper;
import team.project.module.community.display.model.request.AddDepartmentReq;
import team.project.module.community.display.model.request.AlterDepartmentNameReq;
import team.project.module.community.display.model.view.DepartmentView;
import team.project.module.community.display.service.DepartmentService;
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
    public List<DepartmentView> getAllDepartments() {
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



}
