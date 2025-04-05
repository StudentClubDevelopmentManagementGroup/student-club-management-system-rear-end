package team.project.module.club.report.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.report.internal.model.entity.TblReport;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TblReportMapper extends BaseMapper<TblReport> {
    int createReport(String uploader, Long clubId, String reportFileListId, String reportType);
    int deleteReport(Long reportId, Long clubId);
    int updateReport(Long reportId, Long clubId, String reportFileListId, String reportType);

    String getReportUploader(Long reportId);

    TblReport getReportById(Long reportId);

    Page<TblReport> getReportList(Page<Object> Page, Long clubId);

    Page<TblReport> getMemberReportList(Page<Object> Page, Long clubId, String arrangerId);

    List<TblReport> getReportSummaryList(Long clubId, LocalDateTime startTime, LocalDateTime endTime);
}
