package team.project.module.user.tmp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.tmp.mapper.TblDepartmentMapper;
import team.project.module.user.tmp.model.entity.TblDepartmentDO;

@Service
public class DepartmentService {

    @Autowired
    private TblDepartmentMapper departmentMapper;

    public boolean isDepartmentExist(Long departmentId) {
        for (TblDepartmentDO department : departmentMapper.selectList(null)) {
            if (department.getId().equals(departmentId)) {
                return true;
            }
        }

        return false;
    }
}
