package team.project.module.club.report.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.controller.response.Response;
import team.project.base.model.view.PageVO;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.report.internal.model.request.ReportListReq;
import team.project.module.club.report.internal.model.view.ReportInfoVO;
import team.project.module.club.report.internal.service.ReportService;

import java.util.List;

@Tag(name = "成果汇报")
@RestController
public class ReportController {
    @Autowired
    ReportService reportService;

    @Autowired
    AuthServiceI authService;

    @Operation(summary = "添加成果汇报")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/report/add")
    Object addReport(@NotNull @RequestParam("report_type") String reportType, @NotNull @RequestParam("club_id") Long clubId, @NotNull @RequestPart("file") MultipartFile[] file) {
        String arrangerId = (String) (StpUtil.getLoginId());
        authService.requireClubMember(arrangerId, clubId, "只有社团成员能进行成果汇报");

        List<String> result = reportService.createReport(arrangerId, clubId, file, reportType);
        return new Response<>(ServiceStatus.SUCCESS).statusText("创建成功").data(result);
    }

    @Operation(summary = "删除成果汇报")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/report/del")
    Object delReport(@NotNull @RequestParam("report_id") Long reportId, @NotNull @RequestParam("club_id") Long clubId) {
        String arrangerId = (String) (StpUtil.getLoginId());
        authService.requireClubMember(arrangerId, clubId, "只有社团成员能删除自己的成果汇报");

        int result = reportService.deleteReport(reportId, clubId);
        return new Response<>(ServiceStatus.SUCCESS).statusText("删除成功").data(result);
    }

    @Operation(summary = "修改成果汇报")
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    @PostMapping("/club/report/update")
    Object updateReport(
            @NotNull @RequestParam("report_id")
            Long reportId,
            @NotNull
            @RequestParam("report_type")
            String reportType,
            @NotNull @RequestParam("club_id")
            Long clubId,
            @NotNull @RequestPart("file") MultipartFile[] file) {
        String arrangerId = (String) (StpUtil.getLoginId());
        authService.requireClubMember(arrangerId, clubId, "只有社团成员能修改自己的成果汇报");

        List<String> result = reportService.updateReport(arrangerId,reportId, clubId, file, reportType);
        return new Response<>(ServiceStatus.SUCCESS).statusText("修改成功").data(result);
    }

    @Operation(summary = "获取成果汇报列表")
    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @PostMapping("/club/report/list")
    Object getReportList(@Valid @RequestBody ReportListReq req) {
        String arrangerId = (String) (StpUtil.getLoginId());
        authService.requireClubMember(arrangerId, req.getClubId(), "只有社团成员能查看自己的成果汇报");
        Page<Object> page = new Page<>(req.getPageNum(), req.getPageSize());
        PageVO<ReportInfoVO> result = reportService.getReportList(page,req.getClubId());
        return new Response<>(ServiceStatus.SUCCESS).statusText("获取成功").data(result);
    }
}
