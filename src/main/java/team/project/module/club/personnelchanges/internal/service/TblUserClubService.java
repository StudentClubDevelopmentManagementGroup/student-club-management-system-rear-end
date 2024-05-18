package team.project.module.club.personnelchanges.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.project.base.model.view.PageVO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.internal.model.query.ClubMemberInfoQO;
import team.project.module.club.personnelchanges.internal.model.query.ClubQO;
import team.project.module.club.personnelchanges.internal.model.view.ClubMemberInfoVO;
import team.project.module.club.personnelchanges.internal.model.view.UserClubInfoVO;

import java.util.List;

public interface TblUserClubService extends IService<TblUserClubDO> {

    void setClubManager(String userId, Long clubId);

    void quashClubManager(String userId, Long clubId);

    void createMember(String userId, Long clubId);

    void quashMember(String userId, Long clubId);

    PageVO<UserMsgDTO> selectClubMember(ClubQO req);

    Boolean selectTheMember(String userId, Long clubId);

    PageVO<ClubMemberInfoVO> selectClubMemberInfo(ClubMemberInfoQO req);

    List<UserClubInfoVO> selectMemberAllClubInfo(String userId);
}
