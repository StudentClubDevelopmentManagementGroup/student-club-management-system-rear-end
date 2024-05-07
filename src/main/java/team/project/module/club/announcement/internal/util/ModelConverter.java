package team.project.module.club.announcement.internal.util;


import org.springframework.stereotype.Component;
import team.project.module.club.announcement.internal.model.entity.AnnouncementDO;
import team.project.module.club.announcement.internal.model.entity.DraftDO;
import team.project.module.club.announcement.internal.model.view.AnnouncementVO;
import team.project.module.club.announcement.internal.model.view.DraftVO;

@Component("club-announcement-util-ModelConverter")
public class ModelConverter {

    public AnnouncementVO toAnnouncementVO(AnnouncementDO announcementDO, String content) {

        AnnouncementVO result = new AnnouncementVO();
        result.setAnnouncementId(announcementDO.getId());
        result.setTitle(announcementDO.getTitle());
        result.setContent(content);

        return result;
    }

    public DraftVO toDraftVO(DraftDO draftDO, String content) {

        DraftVO result = new DraftVO();
        result.setDraftId(draftDO.getDraftId());
        result.setTitle(draftDO.getTitle());
        result.setContent(content);

        return result;
    }
}
