package team.project.module.club.report.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.report.internal.model.entity.TblReport;

@Mapper
public interface TblReportMapper extends BaseMapper<TblReport> {
    int createReport(String uploader, Long clubId, String reportFileListId, String reportType);
    int deleteReport(Long reportId, Long clubId);
    int updateReport(Long reportId, Long clubId, String reportFileListId, String reportType);

    String getReportUploader(Long reportId);

    Page<TblReport> getReportList(Page<Object> Page, Long clubId);
}
