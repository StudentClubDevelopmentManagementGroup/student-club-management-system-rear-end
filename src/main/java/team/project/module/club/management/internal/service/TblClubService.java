package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.project.base.model.view.PageVO;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.query.ClubInfoQO;

public interface TblClubService extends IService<TblClubDO> {


    void createClub(Long departmentId, String name);

    PageVO<TblClubDO> selectByCriteria(ClubInfoQO page);

    int deleteClub(Long departmentId, String name);

    void reuseClub(Long departmentId, String name);

    void deactivateClub(Long departmentId, String name);

    void recoverClub(Long departmentId, String name);

    PageVO<ClubMsgDTO> findAll(ClubInfoQO page);
}
