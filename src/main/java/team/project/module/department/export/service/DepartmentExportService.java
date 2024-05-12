package team.project.module.department.export.service;


import org.springframework.stereotype.Service;

@Service
public interface DepartmentExportService {
    //输入部门id获取部门名字
    String getDepartmentName(Long id);
}
