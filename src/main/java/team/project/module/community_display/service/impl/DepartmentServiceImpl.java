package team.project.module.community_display.service.impl;

import team.project.module.community_display.dao.DepartmentMapper;
import team.project.module.community_display.entity.Department;
import team.project.module.community_display.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {


    @Autowired
    private  DepartmentMapper departmentMapper;


    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.selectAll();
    }
}
