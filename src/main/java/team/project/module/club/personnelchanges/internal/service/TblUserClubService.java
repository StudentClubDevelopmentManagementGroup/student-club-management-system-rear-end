package team.project.module.club.personnelchanges.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.internal.model.query.ClubQO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;


@Service
public interface TblUserClubService extends IService<TblUserClubDO> {

     void setClubManager(String userId, Long clubId);

     void quashClubManager(String userId, Long clubId);

     void createMember(String userId, Long clubId);

     void quashMember(String userId, Long clubId);

     PageVO<UserMsgDTO> selectClubMember(ClubQO req);

     Boolean selectTheMember(String userId, Long clubId);

     PageVO<UserInfoDTO> selectClubMemberInfo(ClubQO req);

}

