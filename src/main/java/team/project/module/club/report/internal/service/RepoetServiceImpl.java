package team.project.module.club.report.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import team.project.module.club.report.internal.mapper.TblReportMapper;
import team.project.module.club.report.internal.model.entity.TblReport;

public class RepoetServiceImpl extends ServiceImpl<TblReportMapper, TblReport> implements ReportService {
    @Autowired
    TblReportMapper reportMapper;
    @Override
    public int createReport(String uploader, Long clubId, Object reportFileList, String reportType) {
        return baseMapper.createReport(uploader, clubId, reportFileList, reportType);
    }

    @Override
    public int deleteReport(Long id) {
        return baseMapper.deleteReport(id);
    }
}
