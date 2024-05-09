package team.project.module.club.announcement.internal.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.announcement.internal.model.entity.AnnDO;
import team.project.module.club.announcement.internal.model.entity.DraftDO;
import team.project.module.club.announcement.internal.model.view.AnnDetailVO;
import team.project.module.club.announcement.internal.model.view.DraftVO;
import team.project.module.user.export.service.UserInfoServiceI;

@Component("club-announcement-util-ModelConverter")
public class ModelConverter {

    @Autowired
    UserInfoServiceI userInfoService;

    public AnnDetailVO toAnnDetailVO(AnnDO announcementDO, String content) {

        AnnDetailVO result = new AnnDetailVO();
        result.setAnnouncementId(announcementDO.getAnnouncementId());
        result.setClubName("【暂无】等待 club 模块提供接口"); /* ljh_TODO */
        result.setAuthorName(userInfoService.getUserName(announcementDO.getAuthorId()));
        result.setPublishTime(announcementDO.getPublishTime());
        result.setTitle(announcementDO.getTitle());
        result.setContent(content);

        return result;
    }

    public DraftVO toDraftVO(DraftDO draftDO, String content, String summary) {

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
