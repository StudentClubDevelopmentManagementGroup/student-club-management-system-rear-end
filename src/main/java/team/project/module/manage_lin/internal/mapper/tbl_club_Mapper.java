package team.project.module.manage_lin.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;

import java.util.List;

/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface tbl_club_Mapper extends BaseMapper<tbl_club_DO> {

    List<tbl_club_DO> findbyname(String name); /* 示例 */

    List<tbl_club_DO> create_club(Long departmentId, String name);

}
