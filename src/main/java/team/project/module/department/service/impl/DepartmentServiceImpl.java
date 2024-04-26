package team.project.module.department.service.impl;

import team.project.module.department.mapper.DepartmentMapper;
import team.project.module.department.model.request.AddDepartmentReq;
import team.project.module.department.model.request.AlterDepartmentNameReq;
import team.project.module.department.model.view.DepartmentVO;
import team.project.module.department.service.DepartmentService;
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
