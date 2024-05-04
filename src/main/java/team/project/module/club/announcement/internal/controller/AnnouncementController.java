package team.project.module.club.announcement.internal.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.announcement.internal.model.request.UploadAnnouncementReq;
import team.project.module.club.announcement.internal.service.AnnouncementService;

@Tag(name="公告")
@RestController
public class AnnouncementController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AnnouncementService announcementService;

    @Operation(summary="上传公告（未实现）")
    @PostMapping("/announcement/upload")
    // @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object upload(@Valid @RequestBody UploadAnnouncementReq req) {

        // String authorId = (String)( StpUtil.getSession().getLoginId() );

        // announcementService.upload(authorId, req);

        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }
}
