package team.project.module.community_display.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import team.project.module.community_display.entity.Department;
import java.util.List;
@Mapper


public interface DepartmentMapper extends BaseMapper<Department> {

    // 定义 selectAll() 方法
    List<Department> selectAll();
    // 增加院系
    Integer insertDepartment(Department department);

    // 删除院系
    Integer deleteDepartmentByName(String name);

    // 修改院系名
    Integer updateDepartmentName(Department department);

}
