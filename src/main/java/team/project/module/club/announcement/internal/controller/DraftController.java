package team.project.module.club.announcement.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.model.request.SaveDraftReq;
import team.project.module.club.announcement.internal.service.DraftService;

@Tag(name="公告（草稿）")
@RestController
@RequestMapping("/club/announcement/draft")
public class DraftController {

    @Autowired
    AuthServiceI authService;

    @Autowired
    DraftService draftService;

    @Operation(summary="保存草稿")
    @PostMapping("/save")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object save(@Valid @RequestBody SaveDraftReq req) {

        String authorId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(authorId, req.getClubId(), "只有社团负责人能编辑公告草稿");

        draftService.save(authorId, req);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="获取某篇草稿的内容")
    @GetMapping("/read")
    Object read() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary="查看我的草稿箱（分页查询、模糊查询）")
    @GetMapping("/list")
    Object list() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary="删除草稿")
    @PostMapping("/del")
    Object del() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }
}
