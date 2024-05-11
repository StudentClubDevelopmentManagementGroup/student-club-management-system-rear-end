package team.project.module.club.announcement.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.announcement.internal.model.request.UploadAnnouncementReq;
import team.project.module.club.personnelchanges.export.service.PceIService;

@Service
public class AnnouncementService {

    @Autowired
    PceIService clubMemberRoleService;

    public void upload(String authorId, UploadAnnouncementReq req) {
        /*if ( ! clubMemberRoleService.isClubManager(authorId, req.getClubId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "公告发布者不是该社团的负责人");
        }*/
    }
}
