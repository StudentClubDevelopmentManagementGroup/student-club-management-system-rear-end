package team.project.module.club.management.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;

import java.util.List;

@Mapper
public interface TblClubMapper extends BaseMapper<TblClubDO> {

    List<TblClubDO> findByNameAndDepartmentId(Long departmentId, String name);

    Page<TblClubDO> selectByNameAndDepartmentId(@Param("page") Page<TblClubDO> page, Long departmentId, String name);

    Page<TblClubDO> selectByName(@Param("page") Page<TblClubDO> page, String name);

    int createClub(Long departmentId, String name);

    int deleteClub(Long departmentId, String name);

    int reuseClub(Long departmentId, String name);

    int deactivateClub(Long departmentId, String name);

    int recoverClub(Long departmentId, String name);

    Page<TblClubDO> selectByDepartmentId(Page<TblClubDO> page, Long departmentId);

    Page<ClubMsgDTO> findAll(Page<ClubMsgDTO> page);

    Page<ClubMsgDTO> findAllByDepartmentId(Page<ClubMsgDTO> page, Long departmentId);

    Page<ClubMsgDTO> findAllByName(Page<ClubMsgDTO> page, String name);

    Page<ClubMsgDTO> findAllByDepartmentIdAndName(Page<ClubMsgDTO> page, Long departmentId, String name);

    TblClubDO selectById(Long id);
}
