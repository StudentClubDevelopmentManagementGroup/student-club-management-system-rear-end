package team.project.module.department.export.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.department.export.service.DepartmentExportService;
import team.project.module.department.internal.mapper.DepartmentMapper;
import team.project.module.department.internal.model.entity.Department;

@Service
public class DepartmentExportServiceImpl implements DepartmentExportService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public  String getDepartmentName(Long id) {
        Department department = departmentMapper.selectById(id);
        return department.getFullName();

    }

}
