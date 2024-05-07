package team.project.module.club.announcement.internal.util;


import org.springframework.stereotype.Component;
import team.project.module.club.announcement.internal.model.entity.AnnouncementDO;
import team.project.module.club.announcement.internal.model.view.AnnouncementVO;

@Component("club-announcement-util-ModelConverter")
public class ModelConverter {

    public AnnouncementVO toAnnouncementVO(AnnouncementDO announcementDO, String content) {

        AnnouncementVO result = new AnnouncementVO();
        result.setAnnouncementId(announcementDO.getId());
        result.setTitle(announcementDO.getTitle());
        result.setContent(content);

        return result;
    }
}
