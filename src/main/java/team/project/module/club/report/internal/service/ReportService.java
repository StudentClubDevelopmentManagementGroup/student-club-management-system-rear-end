package team.project.module.club.report.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.module.club.report.internal.model.entity.TblReport;

@Service
public interface ReportService extends IService<TblReport> {
    int createReport(String uploader, Long clubId, Object reportFileList, String reportType);
    int deleteReport(Long id);
}
