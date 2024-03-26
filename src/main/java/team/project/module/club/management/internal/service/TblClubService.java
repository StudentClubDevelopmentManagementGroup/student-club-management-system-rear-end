package team.project.module.club.management.internal.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.view.ClubMasVO;

@Service
public  interface TblClubService extends IService<TblClubDO> {


     void createClub(Long departmentId, String name);

     Page<TblClubDO> selectByNameAndDepartmentId(@Param("page") Page<TblClubDO> page, Long departmentId, String name);

     Page<TblClubDO> selectByName(@Param("page") Page<TblClubDO> page, String name);

     Page<TblClubDO> selectByDepartmentId(@Param("page") Page<TblClubDO> page, Long departmentId);

     void deleteClub(Long departmentId, String name);

     void reuseClub(Long departmentId, String name);

     void deactivateClub(Long departmentId, String name);

     void recoverClub(Long departmentId, String name);

     Page<ClubMasVO> findAll(@Param("page") Page<ClubMasVO> page);

     Page<ClubMasVO> findAllByDepartmentId(@Param("page") Page<ClubMasVO> page,Long departmentId);

     Page<ClubMasVO> findAllByName(@Param("page") Page<ClubMasVO> page,String name);

     Page<ClubMasVO> findAllByDepartmentIdAndName(@Param("page") Page<ClubMasVO> page,Long departmentId,String name);

}

