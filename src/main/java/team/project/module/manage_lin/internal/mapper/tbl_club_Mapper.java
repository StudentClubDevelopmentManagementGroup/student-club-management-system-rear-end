package team.project.module.manage_lin.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;

import java.util.List;

/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface tbl_club_Mapper extends BaseMapper<tbl_club_DO> {


//    Page<User> selectPageByUser(@Param("page") Page<User> page, @Param("age") Integer age);

    int create_club(Long departmentId, String name);

    List<tbl_club_DO> findbynamebetweendepartmentId(Long departmentId, String name);


    Page<tbl_club_DO> selectPageBynamebetweendepartmentId(@Param("page") Page<tbl_club_DO> page, Long departmentId, String name);

    Page<tbl_club_DO> selectPageByname(@Param("page") Page<tbl_club_DO> page,  String name);


    int delete_club(Long departmentId, String name);

    int reuse_club(Long departmentId, String name);

    int deactivate_clb(Long departmentId, String name);

    int recover_club(Long departmentId, String name);

    Page<tbl_club_DO> selectPageBydepartmentId(Page<tbl_club_DO> page, Long departmentId);
}
