package team.project.module.club.management.internal.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.base.model.PageVO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.request.TblClubReq;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
@Service
public interface TblClubService extends IService<TblClubDO> {


     void createClub(Long departmentId, String name);
     PageVO<TblClubDO> selectByNameAndDepartmentId(TblClubReq page);

     PageVO<TblClubDO> selectByName(TblClubReq page);

     PageVO<TblClubDO> selectByDepartmentId(TblClubReq page);

     void deleteClub(Long departmentId, String name);

     void reuseClub(Long departmentId, String name);

     void deactivateClub(Long departmentId, String name);

     void recoverClub(Long departmentId, String name);

     PageVO<ClubMsgDTO> findAll(TblClubReq page);

     PageVO<ClubMsgDTO> findAllByDepartmentId(TblClubReq page);

     PageVO<ClubMsgDTO> findAllByName(TblClubReq page);

     PageVO<ClubMsgDTO> findAllByDepartmentIdAndName(TblClubReq page);

}

