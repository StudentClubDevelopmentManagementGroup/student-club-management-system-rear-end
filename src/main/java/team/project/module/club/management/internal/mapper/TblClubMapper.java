package team.project.module.club.management.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;

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

    @Select("SELECT id FROM tbl_club WHERE name = #{name}")
    Long selectClubIdByName(@Param("name") String name);

    TblClubDO selectById(Long id);

    default TblClubDO selectByNameAndDepartmentId(String name, Long departmentId) {
        List<TblClubDO> userList = this.selectList(new LambdaQueryWrapper<TblClubDO>()
                .eq(TblClubDO::getName, name)
                .eq(TblClubDO::getDepartmentId, departmentId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }
}
