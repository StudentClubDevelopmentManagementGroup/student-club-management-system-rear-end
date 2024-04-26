package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import team.project.base.model.PageVO;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.query.ClubInfoQO;
@Service
public interface TblClubService extends IService<TblClubDO> {


     void createClub(Long departmentId, String name);
     PageVO<TblClubDO> selectByNameAndDepartmentId(ClubInfoQO page);

     PageVO<TblClubDO> selectByName(ClubInfoQO page);

     PageVO<TblClubDO> selectByDepartmentId(ClubInfoQO page);

     void deleteClub(Long departmentId, String name);

     void reuseClub(Long departmentId, String name);

     void deactivateClub(Long departmentId, String name);

     void recoverClub(Long departmentId, String name);

     PageVO<ClubMsgDTO> findAll(ClubInfoQO page);

     PageVO<ClubMsgDTO> findAllByDepartmentId(ClubInfoQO page);

     PageVO<ClubMsgDTO> findAllByName(ClubInfoQO page);

     PageVO<ClubMsgDTO> findAllByDepartmentIdAndName(ClubInfoQO page);

}

