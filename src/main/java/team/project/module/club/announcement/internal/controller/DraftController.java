package team.project.module.club.announcement.internal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.project.base.controller.queryparam.QueryParam;
import team.project.base.controller.response.Response;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.club.announcement.internal.model.request.DraftSaveReq;
import team.project.module.club.announcement.internal.model.view.DraftVO;
import team.project.module.club.announcement.internal.service.DraftService;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Tag(name="公告（草稿箱）")
@RestController
@RequestMapping("/club/announcement/draft")
public class DraftController {

    @Autowired
    DraftService draftService;

    @Operation(summary="将公告保存到草稿箱", description="""
        club_id：社团编号，指明该公告所属的社团
        title：公告标题
        content：公告内容（用于详情页）
        summary：内容摘要（用于列表页）
        draft_id：草稿编号，如果传 null 则创建新草稿；如果指定 draft_id 则更新草稿
    """)
    @PostMapping("/save")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object save(@Valid @RequestBody DraftSaveReq req) {

        String authorId = (String)( StpUtil.getLoginId() );
        req.getDraft().setAuthorId(authorId);

        if (req.getDraftId() == null) {
            draftService.createDraft(req.getDraft());
        } else {
            draftService.updateDraft(req);
        }

        return new Response<>(ServiceStatus.SUCCESS).statusText("保存成功");
    }

    @Operation(summary="获取某篇草稿的内容")
    @GetMapping("/read")
    @SaCheckLogin
    Object read(@NotNull(message="未指定草稿id") Long draftId) {

        String authorId = (String)( StpUtil.getLoginId() );

        DraftVO result = draftService.readDraft(authorId, draftId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="查看我的草稿箱（分页查询）", description="""
        数据量不大，暂不提供模糊查询
    """)
    @GetMapping("/list")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object list(
        @NotNull(message="未指定社团id") @ClubIdConstraint
        @RequestParam("club_id") Long clubId,

        @Valid @QueryParam PagingQueryReq pageReq
    ) {
        String authorId = (String)( StpUtil.getLoginId() );

        PageVO<DraftVO> result = draftService.listMyDraft(pageReq, authorId, clubId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="删除草稿")
    @PostMapping("/del")
    @SaCheckLogin
    Object del(@NotNull(message="未指定草稿id") Long draftId) {

        String authorId = (String)( StpUtil.getLoginId() );

        draftService.deleteDraft(authorId, draftId);
        return new Response<>(ServiceStatus.SUCCESS);
    }

    @Operation(summary="清空我的草稿箱")
    @PostMapping("/clear")
    @SaCheckLogin
    Object clear(
        @NotNull(message="未指定社团id") @ClubIdConstraint
        @RequestParam("club_id") Long clubId
    ) {
        String userId = (String)( StpUtil.getLoginId() );

        draftService.delAllMyDraft(userId, clubId);
        return new Response<>(ServiceStatus.SUCCESS);
    }
}
