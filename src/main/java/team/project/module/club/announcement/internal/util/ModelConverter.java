package team.project.module.club.announcement.internal.util;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.announcement.internal.model.entity.AnnDO;
import team.project.module.club.announcement.internal.model.entity.DraftDO;
import team.project.module.club.announcement.internal.model.query.AnnSearchQO;
import team.project.module.club.announcement.internal.model.request.AnnSearchReq;
import team.project.module.club.announcement.internal.model.view.AnnDetailVO;
import team.project.module.club.announcement.internal.model.view.DraftVO;
import team.project.module.club.announcement.tmp.ClubIdMapper;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.export.service.ManagementIService;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.service.UserInfoServiceI;

import java.util.ArrayList;
import java.util.List;

@Component("club-announcement-util-ModelConverter")
public class ModelConverter {

    @Autowired
    ManagementIService clubInfoService;

    @Autowired
    UserInfoServiceI userInfoService;

    @Autowired
    ClubIdMapper clubIdMapper;

    public AnnDetailVO toAnnDetailVO(AnnDO announcementDO, String content, String summary) {
        assert announcementDO != null;

        ClubBasicMsgDTO clubInfo = clubInfoService.selectClubBasicMsg(announcementDO.getClubId());

        AnnDetailVO result = new AnnDetailVO();
        result.setAnnouncementId(announcementDO.getAnnouncementId());
        result.setDepartmentName(clubInfo.getDepartmentName());
        result.setClubName(clubInfo.getName());
        result.setAuthorId(announcementDO.getAuthorId());
        result.setAuthorName(userInfoService.getUserName(announcementDO.getAuthorId()));
        result.setPublishTime(announcementDO.getPublishTime());
        result.setTitle(announcementDO.getTitle());
        result.setContent(content);
        result.setSummary(summary);

        return result;
    }

    public DraftVO toDraftVO(DraftDO draftDO, String content, String summary) {
        assert draftDO != null;

        DraftVO result = new DraftVO();
        result.setDraftId(draftDO.getDraftId());
        result.setCreateTime(draftDO.getCreateTime());
        result.setUpdateTime(draftDO.getUpdateTime());
        result.setTitle(draftDO.getTitle());
        result.setContent(content);
        result.setSummary(summary);

        return result;
    }

    public AnnSearchQO toAnnSearchQO(AnnSearchReq searchReq) {
        assert searchReq != null;

        List<Long> clubIdList = new ArrayList<>();
        if (null != searchReq.getClubId())
            clubIdList.add(searchReq.getClubId());       /* <- 一旦指定 club_id，则忽略 club_name */
        else if (null != searchReq.getClubName() && ! searchReq.getClubName().isBlank())
            clubIdList.addAll( clubIdMapper.searchClub(searchReq.getClubName()) );

        List<String> authorIdList = new ArrayList<>();
        if (null != searchReq.getAuthorId())
            authorIdList.add(searchReq.getAuthorId());   /* <- 一旦指定 author_id，则忽略 author_name */
        else if (null != searchReq.getAuthorName() && ! searchReq.getAuthorName().isBlank()) {
            for (UserBasicInfoDTO author : userInfoService.searchUser(searchReq.getAuthorName()))
                authorIdList.add(author.getUserId());
        }

        String titleKeyword = searchReq.getTitleKeyword() == null || searchReq.getTitleKeyword().isBlank()
                                ? null : searchReq.getTitleKeyword();

        AnnSearchQO searchQO = new AnnSearchQO();
        searchQO.setClubIdList(clubIdList);
        searchQO.setAuthorIdList(authorIdList);
        searchQO.setTitleKeyword(titleKeyword);
        searchQO.setFromDate(searchReq.getFromDate());
        searchQO.setToDate(searchReq.getToDate());

        return searchQO;
    }
}
