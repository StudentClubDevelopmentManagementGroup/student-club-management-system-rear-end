package team.project.module.club.personnelchanges.export.service;

import team.project.base.model.PageVO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.export.model.request.ClubReq;

public interface PceIService {
    boolean isClubManager(String userId, Long clubId);

    // TODO: boolean isClubMember(String userId, Long clubId);
    PageVO<UserMsgDTO> selectClubMember(ClubReq req);

    Boolean selectTheMember(String userId, Long clubId);
}
