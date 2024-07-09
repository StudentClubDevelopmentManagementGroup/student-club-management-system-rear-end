package team.project.module.club.report.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.report.internal.model.entity.TblReport;

@Mapper
public interface TblReportMapper extends BaseMapper<TblReport> {
    int createReport(String uploader, Long clubId, String reportFileListId, String reportType);
    int deleteReport(Long id);
}
