package team.project.module.club.report.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.report.internal.model.request.ReportReq;
import team.project.module.club.report.internal.service.ReportService;

@Tag(name = "成果汇报")
@RestController
public class ReportController {
    @Autowired
    ReportService reportService;

    @Autowired
    AuthServiceI authService;
    @Operation(summary = "添加小组成员")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/report/add")
    Object addReport(@Valid @RequestBody ReportReq req) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        authService.requireClubMember(arrangerId, req.getClubId(), "只有社团成员能进行成果汇报");

        reportService.createReport(arrangerId, req.getClubId(), req.getFile(), req.getReportType());
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功");
    }
}
