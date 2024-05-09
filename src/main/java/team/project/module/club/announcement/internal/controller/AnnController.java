package team.project.module.club.announcement.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.model.request.PublishAnnReq;
import team.project.module.club.announcement.internal.model.view.AnnDetailVO;
import team.project.module.club.announcement.internal.service.AnnService;

import java.util.List;

@Tag(name="公告")
@RestController
@RequestMapping("/club/announcement")
public class AnnController {

    @Autowired
    AuthServiceI authService;

    @Autowired
    AnnService announcementService;

    @Operation(summary="发布公告") /* ljh_TODO: 介绍 */
    @PostMapping("/publish")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object publish(@Valid @RequestBody PublishAnnReq req) {

        String authorId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(authorId, req.getClubId(), "只有社团负责人能发布公告");

        announcementService.publishAnnouncement(authorId, req);
        return new Response<>(ServiceStatus.CREATED).statusText("发布成功");
    }

    @Operation(summary="获取某篇公告的内容")
    @GetMapping("/read")
    Object read(@NotNull(message="未指定公告id") Long announcementId) {
        AnnDetailVO result = announcementService.readAnnouncement(announcementId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="公告列表（分页查询、模糊查询）")
    @GetMapping("/list")
    Object list() {
        if (true) return new Response<>(ServiceStatus.NOT_IMPLEMENTED);

        List<AnnDetailVO> result = announcementService.list();
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }
}
