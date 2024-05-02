package team.project.module.user.tmp.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import team.project.module.user.tmp.mapper.TblDepartmentMapper;
import team.project.module.user.tmp.model.entity.TblDepartmentDO;

import java.util.HashMap;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private TblDepartmentMapper departmentMapper;

    @Autowired
    team.project.module.department.service.DepartmentService departmentService; /* <- tmp */

    /* 院系表长期不变动，用缓存避免多次查询数据库
       一旦院系表发生变动，则需要重启 java 后端 */
    private HashMap<Long, TblDepartmentDO> cache;

    // @PostConstruct
    private void selectAllDepartment() {
        List<TblDepartmentDO> departmentList = departmentMapper.selectList(null);
        cache = new HashMap<>();
        for (TblDepartmentDO department : departmentList) {
            cache.put(department.getId(), department);
        }
        Assert.isTrue( ! cache.isEmpty(), "获取院系信息失败");
    }

    public boolean isDepartmentExist(Long departmentId) {
        return cache.containsKey(departmentId);
    }

    public String getNameById(Long departmentId) {
        TblDepartmentDO tblDepartmentDO = cache.get(departmentId);
        // return tblDepartmentDO.getFullName();
        return departmentService.getDepartmentName(departmentId);
    }
}
