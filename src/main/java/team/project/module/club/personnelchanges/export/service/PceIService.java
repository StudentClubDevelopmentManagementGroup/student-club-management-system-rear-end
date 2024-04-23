package team.project.module.club.personnelchanges.export.service;


/* 实现类记得加上 @Service */
/* 注意接口是以 "IService" 结尾 */

import team.project.base.model.PageVO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.export.model.request.ClubReq;

public interface PceIService {
    boolean isClubManager(String userId, Long clubId);

    // TODO: boolean isClubMember(String userId, Long clubId);
    PageVO<UserMsgDTO> selectClubMember(ClubReq req);

    Boolean selectTheMember(String userId, Long clubId);

    /* 注意这里的 DTO 和 QO 都是 export 包下的，不是 internal 包下的 */
}
