package team.project.module.club.report.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.model.view.PageVO;
import team.project.module.club.report.internal.model.entity.TblReport;
import team.project.module.club.report.internal.model.view.ReportInfoVO;

import java.util.List;

@Service
public interface ReportService extends IService<TblReport> {
    List<String> createReport(String uploader, Long clubId, MultipartFile[] reportFileList, String reportType);
    int deleteReport(Long uploaderId, Long clubId);
    List<String> updateReport(String uploader,Long uploaderId, Long clubId, MultipartFile[] reportFileList, String reportType);
    PageVO<ReportInfoVO> getReportList(Page<Object> page, Long clubId);
}
