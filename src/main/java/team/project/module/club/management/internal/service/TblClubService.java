package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.query.TblClubQO;
@Service
public interface TblClubService extends IService<TblClubDO> {


     void createClub(Long departmentId, String name);
     PageVO<TblClubDO> selectByNameAndDepartmentId(TblClubQO page);

     PageVO<TblClubDO> selectByName(TblClubQO page);

     PageVO<TblClubDO> selectByDepartmentId(TblClubQO page);

     void deleteClub(Long departmentId, String name);

     void reuseClub(Long departmentId, String name);

     void deactivateClub(Long departmentId, String name);

     void recoverClub(Long departmentId, String name);

     PageVO<ClubMsgDTO> findAll(TblClubQO page);

     PageVO<ClubMsgDTO> findAllByDepartmentId(TblClubQO page);

     PageVO<ClubMsgDTO> findAllByName(TblClubQO page);

     PageVO<ClubMsgDTO> findAllByDepartmentIdAndName(TblClubQO page);

}

