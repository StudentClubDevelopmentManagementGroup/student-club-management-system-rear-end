package team.project.module.club.management.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.view.ClubMasVO;

import java.util.List;

/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface TblClubMapper extends BaseMapper<TblClubDO> {


//    Page<User> selectPageByUser(@Param("page") Page<User> page, @Param("age") Integer age);



    List<TblClubDO> findByNameAndDepartmentId(Long departmentId, String name);


    Page<TblClubDO> selectByNameAndDepartmentId(@Param("page") Page<TblClubDO> page, Long departmentId, String name);

    Page<TblClubDO> selectByName(@Param("page") Page<TblClubDO> page, String name);

    int createClub(Long departmentId, String name);

    int deleteClub(Long departmentId, String name);

    int reuseClub(Long departmentId, String name);

    int deactivateClub(Long departmentId, String name);

    int recoverClub(Long departmentId, String name);

    Page<TblClubDO> selectByDepartmentId(Page<TblClubDO> page, Long departmentId);

    Page<ClubMasVO> findAll(Page<ClubMasVO> page);
}
