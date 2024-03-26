package team.project.module.community.display.service.impl;

import team.project.module.community.display.entity.Department;
import team.project.module.community.display.mapper.DepartmentMapper;
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
    public List<Department> getAllDepartments() {
        return departmentMapper.selectAll();
    }

    @Override
    public boolean addDepartment(Department department) {
        int rowsAffected = departmentMapper.addDepartment(department);
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteDepartmentByName(String name) {
        int rowsAffected = departmentMapper.deleteDepartmentByName(name);
        return rowsAffected > 0;
    }

    @Override
    public boolean updateDepartment(Department department) {
        int rowsAffected = departmentMapper.updateDepartmentName(department);
        return rowsAffected > 0;
    }

}
