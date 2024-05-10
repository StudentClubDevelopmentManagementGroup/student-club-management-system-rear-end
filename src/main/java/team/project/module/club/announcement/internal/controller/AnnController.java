package team.project.module.club.announcement.internal.controller;

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
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.model.request.AnnPublishReq;
import team.project.module.club.announcement.internal.model.request.AnnSearchReq;
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

    @Operation(summary="公告列表（分页查询、模糊查询）")
    @GetMapping("/search")
    Object search(
        @Valid @QueryParam AnnSearchReq searchReq,
        @Valid @QueryParam PagingQueryReq pageReq
    ) {
        List<AnnDetailVO> result = announcementService.search(pageReq, searchReq);
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }

    @Operation(summary="公告列表（全查，测试用）")
    @GetMapping("/tmp_list")
    Object tmpList() {

        List<AnnDetailVO> result = announcementService.tmpList();
        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }
}
