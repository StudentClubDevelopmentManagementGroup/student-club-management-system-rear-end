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
import team.project.module.club.announcement.internal.model.request.SaveDraftReq;
import team.project.module.club.announcement.internal.model.view.DraftVO;
import team.project.module.club.announcement.internal.service.DraftService;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import java.util.List;

@Tag(name="公告（草稿）")
@RestController
@RequestMapping("/club/announcement/draft")
public class DraftController {

    @Autowired
    AuthServiceI authService;

    @Autowired
    DraftService draftService;

    @Operation(summary="将公告保存到草稿箱")
    @PostMapping("/save")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object save(@Valid @RequestBody SaveDraftReq req) {

        String authorId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(authorId, req.getClubId(), "只有社团负责人能编辑公告");

        /* draft_id 为空则创建新的草稿，不为空则更新草稿 */
        if (req.getDraftId() == null) {
            draftService.createDraft(authorId, req);
        }
        else {
            draftService.updateDraft(authorId, req);
        }

        return new Response<>(ServiceStatus.SUCCESS).statusText("保存成功");
    }

    @Operation(summary="获取某篇草稿的内容")
    @GetMapping("/read")
    Object read(@NotNull(message="未指定草稿id") Long draftId) {

        String authorId = (String)( StpUtil.getLoginId() ); /* ljh_TODO 身份验证 */

        DraftVO result = draftService.readDraft(draftId);

        if (result != null) {
            return new Response<>(ServiceStatus.SUCCESS).data(result);
        } else {
            return new Response<>(ServiceStatus.NOT_FOUND);
        }
    }

    @Operation(summary="查看我的草稿箱（分页查询、模糊查询）")
    @GetMapping("/list")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object list(
        @NotNull(message="未指定社团id")
        @ClubIdConstraint
        @RequestParam("club_id") Long clubId
    ) {
        String authorId = (String)( StpUtil.getLoginId() );
        authService.requireClubManager(authorId, clubId, "只有社团负责人能编辑公告");

        List<DraftVO> result = draftService.list(authorId, clubId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="删除草稿")
    @PostMapping("/del")
    Object del() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }
}
