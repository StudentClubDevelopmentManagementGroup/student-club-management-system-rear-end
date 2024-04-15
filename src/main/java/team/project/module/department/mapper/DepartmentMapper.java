package team.project.module.department.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import team.project.module.department.model.entity.Department;
import team.project.module.department.model.request.AddDepartmentReq;
import team.project.module.department.model.request.AlterDepartmentNameReq;
import team.project.module.department.model.view.DepartmentVO;

import java.util.List;

@Mapper


public interface DepartmentMapper extends BaseMapper<Department> {

    // 定义 selectAll() 方法
    List<DepartmentVO> selectAll();

    // 增加院系
    Integer addDepartment(AddDepartmentReq adddepartmentreq);


    // 删除院系
    Integer deleteDepartmentByName(String name);

    // 修改院系名
    Integer updateDepartmentName(AlterDepartmentNameReq alterDepartmentNameReq);

}
