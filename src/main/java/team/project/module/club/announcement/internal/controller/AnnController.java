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
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.model.request.AnnPublishReq;
import team.project.module.club.announcement.internal.model.request.AnnSearchReq;
import team.project.module.club.announcement.internal.model.view.AnnDetailVO;
import team.project.module.club.announcement.internal.service.AnnService;

@Tag(name="公告")
@RestController
@RequestMapping("/club/announcement")
public class AnnController {

    @Autowired
    AuthServiceI authService;

    @Autowired
    AnnService announcementService;

    @Operation(summary="发布公告", description="""
        club_id：社团编号，指明该公告所属的社团
        title：公告标题
        content：公告内容（用于详情页）
        summary：内容摘要（用于列表页）
        draft_id：草稿编号，若不为 null 则发布公告后顺带删除该草稿，若为 null 则直接发布公告
    """)
    @PostMapping("/publish")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object publish(@Valid @RequestBody AnnPublishReq req) {

        String authorId = (String)( StpUtil.getLoginId() );
        req.getAnnouncement().setAuthorId(authorId);

        announcementService.publishAnn(req);
        return new Response<>(ServiceStatus.CREATED).statusText("发布成功");
    }

    @Operation(summary="获取某篇公告的内容")
    @GetMapping("/read")
    Object read(@NotNull(message="未指定公告id") Long announcementId) {
        AnnDetailVO result = announcementService.readAnn(announcementId);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="搜索公告（分页查询、模糊查询）", description="""
        club_id：社团编号，查询指定社团发布的公告（传 null 或 "" 表示全匹配）
        title_keyword：标题检索关键字，查询标题中包含该关键字的公告（传 null 或 "" 表示全匹配）
        page_num：分页查询，当前页码
        page_size：分页查询，页大小
    """) // ljh_TODO 增加按时间检索
    @GetMapping("/search")
    Object search(
        @Valid @QueryParam AnnSearchReq searchReq,
        @Valid @QueryParam PagingQueryReq pageReq
    ) {
        PageVO<AnnDetailVO> result = announcementService.searchAnn(pageReq, searchReq);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="删除公告")
    @PostMapping("/del")
    @SaCheckLogin
    Object del(@NotNull(message="未指定公告id") Long announcementId) {
        String userId = (String) (StpUtil.getLoginId());

        announcementService.deleteAnn(userId, announcementId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功");
    }

    @Operation(summary="移入草稿箱", hidden=true)
    @PostMapping("/to_draft")
    @SaCheckLogin
    Object toDraft(@NotNull(message="未指定公告id") Long announcementId) {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }
}
