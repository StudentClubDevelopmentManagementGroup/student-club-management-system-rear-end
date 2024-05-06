package team.project.module.club.announcement.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.model.request.UploadAnnouncementReq;
import team.project.module.club.announcement.internal.model.view.AnnouncementVO;
import team.project.module.club.announcement.internal.service.AnnouncementService;

@Tag(name="公告")
@RestController
public class AnnouncementController {

    @Autowired
    AuthServiceI authService;

    @Autowired
    AnnouncementService announcementService;

    @Operation(summary="发布公告")
    @PostMapping("/announcement/publish")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object publish(@Valid @RequestBody UploadAnnouncementReq req) {

        String authorId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(authorId, req.getClubId(), "只有社团负责人能发布公告");

        long announcementId = announcementService.upload(authorId, req);

        return new Response<>(ServiceStatus.CREATED).statusText("发布成功").data(announcementId);
    }

    @Operation(summary="获取公告内容")
    @GetMapping("/announcement/read")
    Object publish(@NotNull(message="未指定公告id") Long announcementId) {

        String userId = (String)( StpUtil.getLoginIdDefaultNull() );

        AnnouncementVO result = announcementService.read(userId, announcementId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }
}
