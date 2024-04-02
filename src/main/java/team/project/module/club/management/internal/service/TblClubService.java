package team.project.module.club.management.internal.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.request.TblClubReq;
import team.project.module.club.management.internal.model.view.ClubMasVO;

@Service
public interface TblClubService extends IService<TblClubDO> {


     void createClub(Long departmentId, String name);
     Page<TblClubDO> selectByNameAndDepartmentId(TblClubReq page);

     Page<TblClubDO> selectByName(TblClubReq page);

     Page<TblClubDO> selectByDepartmentId(TblClubReq page);

     void deleteClub(Long departmentId, String name);

     void reuseClub(Long departmentId, String name);

     void deactivateClub(Long departmentId, String name);

     void recoverClub(Long departmentId, String name);

     Page<ClubMasVO> findAll( TblClubReq page);

     Page<ClubMasVO> findAllByDepartmentId(TblClubReq page);

     Page<ClubMasVO> findAllByName(TblClubReq page);

     Page<ClubMasVO> findAllByDepartmentIdAndName(TblClubReq page);

}

