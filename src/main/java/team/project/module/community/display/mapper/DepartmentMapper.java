package team.project.module.community.display.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import team.project.module.community.display.model.entity.Department;
import team.project.module.community.display.model.request.AddDepartmentReq;
import team.project.module.community.display.model.request.AlterDepartmentNameReq;
import team.project.module.community.display.model.view.DepartmentView;

import java.util.List;

@Mapper


public interface DepartmentMapper extends BaseMapper<Department> {

    // 定义 selectAll() 方法
    List<DepartmentView> selectAll();
    // 增加院系
    Integer addDepartment(AddDepartmentReq adddepartmentreq);


    // 删除院系
    Integer deleteDepartmentByName(String name);

    // 修改院系名
    Integer updateDepartmentName(AlterDepartmentNameReq alterDepartmentNameReq);

}
