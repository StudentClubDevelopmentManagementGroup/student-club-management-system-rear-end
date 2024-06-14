package team.project.module.club.announcement.internal.util;

import org.apache.commons.lang3.StringUtils;
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

import java.util.HashSet;

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

        /* TODO ljh_TODO selectClubBasicMsg 会抛异常 */
        ClubBasicMsgDTO clubInfo = clubInfoService.selectClubBasicMsg(announcementDO.getClubId());

        AnnDetailVO result = new AnnDetailVO();
        result.setAnnouncementId(announcementDO.getAnnouncementId());
        result.setDepartmentName(clubInfo.getDepartmentName());
        result.setClubName(clubInfo.getName());
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
}
