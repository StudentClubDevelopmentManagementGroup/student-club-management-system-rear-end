package team.project.module.department.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import team.project.module.department.internal.model.entity.Department;
import team.project.module.department.internal.model.request.AddDepartmentReq;
import team.project.module.department.internal.model.request.AlterDepartmentNameReq;
import team.project.module.department.internal.model.view.DepartmentVO;

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

    // 使用 MyBatis 的注解进行 SQL 查询
    @Select("SELECT full_name FROM tbl_department WHERE id = #{id}")
    String getDepartmentName(Long id);




}
