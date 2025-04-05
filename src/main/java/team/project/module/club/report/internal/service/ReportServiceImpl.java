package team.project.module.club.report.internal.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.export.service.ManagementIService;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.club.report.internal.mapper.TblReportMapper;
import team.project.module.club.report.internal.model.entity.TblReport;
import team.project.module.club.report.internal.model.view.ReportInfoVO;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;
import team.project.module.util.fileutils.ByteArrayMultipartFile;
import team.project.module.util.fileutils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team.project.module.util.filestorage.export.model.enums.FileStorageType.LOCAL;

@Service
public class ReportServiceImpl extends ServiceImpl<TblReportMapper, TblReport> implements ReportService {
    @Autowired
    TblReportMapper reportMapper;

    @Autowired
    FileStorageServiceI fileStorageServiceI;

    @Autowired
    AuthServiceI authService;

    @Autowired
    ManagementIService managementIService;

    @Autowired
    PceIService pceIService;


    @Override
    public List<String> createReport(String uploader, Long clubId, MultipartFile[] reportFileList, String reportType) {

        JsonObject jsonObjects = new JsonObject();

        FileUtils fileUtils = new FileUtils();

        String uploadFileBasePath = "/report/" + uploader + "/" + reportType + "/";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        List<String> fileIds = new ArrayList<>();
        for (MultipartFile file : reportFileList) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String fileType = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String fileName = FileUtils.getFileNameNoEx(originalFilename) + "_" + formatter.format(date) + fileType; // 使用时间戳避免文件名重复
                UploadFileQO uploadFileQO = new UploadFileQO();
                uploadFileQO.setOverwrite(true);
                uploadFileQO.setTargetFilename(fileName);
                uploadFileQO.setTargetFolder(uploadFileBasePath);
                try {
                    String fileId = fileStorageServiceI.uploadFile(file, LOCAL, uploadFileQO);
                    fileIds.add(fileId);
                    // 添加到 JSON 数组
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("file_name", fileName);
                    jsonObject.addProperty("fileId", fileId);

                    jsonObjects.add("file" + date, jsonObject);
                } catch (FileStorageException e) {
                    fileIds.forEach(fileStorageServiceI::deleteFile);
                    throw new ServiceException(ServiceStatus.CONFLICT, "上传失败");
                }
            } else {
                log.warn("跳过空文件");
            }
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObjects);
        if (1 != reportMapper.createReport(uploader, clubId, jsonString, reportType)) {
            fileIds.forEach(fileStorageServiceI::deleteFile);
            throw new ServiceException(ServiceStatus.CONFLICT, "上传失败");
        } else {
            List<String> fileUrlList = new ArrayList<>();
            for (String fileId : fileIds) {
                // 确保fileId不为空或空白后调用服务方法
                if (StringUtils.isNotBlank(fileId)) {
                    String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                    // 使用获取到的fileUrl进行后续操作，比如打印、保存或进一步处理
                    fileUrlList.add(fileUrl);
                }
            }
            return fileUrlList;
        }
    }

    @Override
    public int deleteReport(Long reportId, Long clubId) {
        String arrangerId = (String) StpUtil.getLoginId();
        String uploader = reportMapper.getReportUploader(reportId);
        if (!uploader.equals(arrangerId)) {
            authService.requireSuperAdmin(arrangerId, "除了超级管理员，只有社团成员能删除自己的成果汇报");
        }

        // 获取报告信息以提取文件ID
        TblReport report = reportMapper.getReportById(reportId);
        if (report == null) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "报告不存在");
        }

        // 获取文件信息Map并提取fileId
        Map<String, Map<String, String>> fileMap = report.getReportFileList();
        List<String> fileIds = new ArrayList<>();
        if (fileMap != null) {
            for (Map<String, String> fileInfo : fileMap.values()) {
                String fileId = fileInfo.get("fileId");
                if (StringUtils.isNotBlank(fileId)) {
                    fileIds.add(fileId.trim());
                }
            }
        }

        // 删除所有关联的文件
        fileIds.forEach(fileId -> {
            try {
                boolean isDeleted = fileStorageServiceI.deleteFile(fileId);
                if (!isDeleted) {
                    throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "文件删除失败");
                }
            } catch (Exception e) {
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "文件删除时异常");
            }
        });

        // 删除数据库中的报告记录
        return baseMapper.deleteReport(reportId, clubId);
    }


    @Override
    public List<String> updateReport(String uploader, Long reportId, Long clubId, MultipartFile[] reportFileList, String reportType) {
        String arrangerId = (String) (StpUtil.getLoginId());
        if (!reportMapper.getReportUploader(reportId).equals(arrangerId)) {
            authService.requireSuperAdmin(arrangerId, "除了超级管理员，只有社团成员能删除自己的成果汇报");
        }
        JsonObject jsonObjects = new JsonObject();
        String uploadFileBasePath = "/report/" + uploader + "/" + reportType + "/";
        int time = 0;
        List<String> fileIds = new ArrayList<>();
        for (MultipartFile file : reportFileList) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String fileType = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String fileName = uploader + "_" + time + fileType; // 使用时间戳避免文件名重复
                time++;
                UploadFileQO uploadFileQO = new UploadFileQO();
                uploadFileQO.setOverwrite(true);
                uploadFileQO.setTargetFilename(fileName);
                uploadFileQO.setTargetFolder(uploadFileBasePath);
                try {
                    String fileId = fileStorageServiceI.uploadFile(file, LOCAL, uploadFileQO);
                    fileIds.add(fileId);
                    // 添加到 JSON 数组
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("file_name", fileName);
                    jsonObject.addProperty("fileId", fileId);

                    jsonObjects.add("file" + time, jsonObject);
                } catch (FileStorageException e) {
                    fileIds.forEach(fileStorageServiceI::deleteFile);
                    throw new ServiceException(ServiceStatus.CONFLICT, "上传失败");
                }
            } else {
                log.warn("跳过空文件");
            }
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObjects);
        if (1 != reportMapper.updateReport(reportId, clubId, jsonString, reportType)) {
            fileIds.forEach(fileStorageServiceI::deleteFile);
            throw new ServiceException(ServiceStatus.CONFLICT, "修改失败");
        } else {
            List<String> fileUrlList = new ArrayList<>();
            for (String fileId : fileIds) {
                // 确保fileId不为空或空白后调用服务方法
                if (StringUtils.isNotBlank(fileId)) {
                    String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                    // 使用获取到的fileUrl进行后续操作，比如打印、保存或进一步处理
                    fileUrlList.add(fileUrl);
                }
            }
            return fileUrlList;
        }
    }

    @Override
    public PageVO<ReportInfoVO> getReportList(Page<Object> page, Long clubId) {
        Page<TblReport> reportPage = reportMapper.getReportList(page, clubId);
        List<ReportInfoVO> reportInfoVOList = new ArrayList<>();

        for (TblReport tblReport : reportPage.getRecords()) {
            ReportInfoVO reportInfoVO = new ReportInfoVO();
            // 设置基本字段...
            reportInfoVO.setId(tblReport.getId());
            reportInfoVO.setReportType(tblReport.getReportType());
            reportInfoVO.setUploader(tblReport.getUploader());
            reportInfoVO.setCreateTime(tblReport.getCreateTime());
            reportInfoVO.setUpdateTime(tblReport.getUpdateTime());
            reportInfoVO.setDeleted(tblReport.getDeleted());
            reportInfoVO.setClubId(tblReport.getClubId());
            // 处理文件列表
            List<String> fileUrlList = new ArrayList<>();
            Map<String, Map<String, String>> fileMap = tblReport.getReportFileList();
            if (fileMap != null) {
                for (Map<String, String> fileInfo : fileMap.values()) {
                    String fileId = fileInfo.get("fileId");
                    if (StringUtils.isNotBlank(fileId)) {
                        String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                        fileUrlList.add(fileUrl);
                    }
                }
            }
            reportInfoVO.setReportFile(fileUrlList);

            reportInfoVOList.add(reportInfoVO);
        }

        return reportPage.getTotal() == 0 ?
                null :
                new PageVO<>(reportInfoVOList, new Page<>(page.getPages(), page.getSize(), reportPage.getTotal()));
    }

    @Override
    public PageVO<ReportInfoVO> getMemberReportList(Page<Object> page, Long clubId, String arrangerId) {
        Page<TblReport> reportPage = reportMapper.getMemberReportList(page, clubId, arrangerId);
        List<ReportInfoVO> reportInfoVOList = new ArrayList<>();

        for (TblReport tblReport : reportPage.getRecords()) {
            ReportInfoVO reportInfoVO = new ReportInfoVO();
            // 设置基本字段...
            reportInfoVO.setId(tblReport.getId());
            reportInfoVO.setReportType(tblReport.getReportType());
            reportInfoVO.setUploader(tblReport.getUploader());
            reportInfoVO.setCreateTime(tblReport.getCreateTime());
            reportInfoVO.setUpdateTime(tblReport.getUpdateTime());
            reportInfoVO.setDeleted(tblReport.getDeleted());
            reportInfoVO.setClubId(tblReport.getClubId());
            // 处理文件列表
            List<String> fileUrlList = new ArrayList<>();
            Map<String, Map<String, String>> fileMap = tblReport.getReportFileList();
            if (fileMap != null) {
                for (Map<String, String> fileInfo : fileMap.values()) {
                    String fileId = fileInfo.get("fileId");
                    if (StringUtils.isNotBlank(fileId)) {
                        String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                        fileUrlList.add(fileUrl);
                    }
                }
            }
            reportInfoVO.setReportFile(fileUrlList);

            reportInfoVOList.add(reportInfoVO);
        }

        return reportPage.getTotal() == 0 ?
                null :
                new PageVO<>(reportInfoVOList, new Page<>(page.getPages(), page.getSize(), reportPage.getTotal()));
    }

    @Override
    @Transactional
    public String getReportSummary(Long clubId, LocalDateTime startTime, LocalDateTime endTime) {
        JsonObject jsonObjects = new JsonObject();

        String fileId;
        try {
            // 1. 获取基础信息
            ClubBasicMsgDTO clubInfo = managementIService.selectClubBasicMsg(clubId);
            String clubName = clubInfo.getName();

            // 获取负责人信息
            String managerNames = pceIService.getAllClubManagers(clubId);
            // 2. 获取成果数据
            List<TblReport> reports = reportMapper.getReportSummaryList(clubId, startTime, endTime);
            // 3. 分类统计（与之前相同）
            Map<String, Long> stats = reports.stream()
                    .collect(Collectors.groupingBy(
                            TblReport::getReportType,
                            Collectors.counting()
                    ));
            String[] categories = {"奖项", "论文", "学习情况", "参加的比赛", "采访", "软著", "其他"};
            for (String category : categories) {
                stats.putIfAbsent(category, 0L);
            }
            // 4. 生成Word文档
            XWPFDocument doc = new XWPFDocument();

            // 标题
            XWPFParagraph titlePara = doc.createParagraph();
            titlePara.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titlePara.createRun();
            titleRun.setText("计算机与信息安全学院基地成果汇报");
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            // 基本信息
            addKeyValue(doc, "基地名称：", clubName);
            addKeyValue(doc, "基地负责人：", managerNames);

            // 成果概述
            XWPFParagraph overviewPara = doc.createParagraph();
            XWPFRun overviewRun = overviewPara.createRun();
            overviewRun.setText("成果汇报概述：");
            overviewRun.setBold(true);
            // 成果统计表格
            XWPFTable table = doc.createTable(8, 2); // 7分类+标题
            table.setWidth("100%");

            // 表头
            setTableHeader(table.getRow(0), "分类", "数量");

            // 填充数据
            int rowIndex = 1;
            for (String category : categories) {
                setTableCell(table.getRow(rowIndex), category, stats.get(category).toString());
                rowIndex++;
            }

            // 原方法片段修改
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            byte[] docBytes = out.toByteArray();
            out.close();
            LocalDateTime date = LocalDateTime.now();
            String fileName = String.format("%s-成果统计-%s.docx",
                    clubName,
                    date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            );
            MultipartFile multipartFile = new ByteArrayMultipartFile(
                    docBytes,
                    "file",
                    fileName,
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );
            // 6. 上传文件
            UploadFileQO uploadQO = new UploadFileQO();
            uploadQO.setOverwrite(true);
            uploadQO.setTargetFilename(fileName);
            uploadQO.setTargetFolder("/report/summary/" + clubId + "/");

            fileId =fileStorageServiceI.uploadFile(multipartFile, LOCAL, uploadQO);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("file_name", fileName);
            jsonObject.addProperty("fileId", fileId);
            jsonObjects.add("file" + date, jsonObject);
        } catch (Exception e) {
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "生成报告失败: " + e.getMessage());
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObjects);
        String uploader = (String)( StpUtil.getLoginId() );
        if (1 != reportMapper.createReport(uploader, clubId, jsonString, "总结")) {
            fileStorageServiceI.deleteFile(fileId);
            throw new ServiceException(ServiceStatus.CONFLICT, "上传失败");
        } else {
            List<String> fileUrlList = new ArrayList<>();
                if (StringUtils.isNotBlank(fileId)) {
                    String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                    // 使用获取到的fileUrl进行后续操作，比如打印、保存或进一步处理
                    fileUrlList.add(fileUrl);
            }
            return fileStorageServiceI.getFileUrl(fileId);
        }
    }
    // 辅助方法：添加键值对段落
    private void addKeyValue(XWPFDocument doc, String key, String value) {
        XWPFParagraph para = doc.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(key);
        run.setBold(true);
        run = para.createRun();
        run.setText(value);
    }
    // 辅助方法：设置表格表头
    private void setTableHeader(XWPFTableRow row, String... headers) {
        for (int i = 0; i < headers.length; i++) {
            XWPFTableCell cell = row.getCell(i);
            cell.removeParagraph(0);
            XWPFParagraph para = cell.addParagraph();
            para.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = para.createRun();
            run.setText(headers[i]);
            run.setBold(true);
        }
    }
    // 辅助方法：填充表格单元格
    private void setTableCell(XWPFTableRow row, String... values) {
        for (int i = 0; i < values.length; i++) {
            XWPFTableCell cell = row.getCell(i);
            cell.removeParagraph(0);
            XWPFParagraph para = cell.addParagraph();
            para.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = para.createRun();
            run.setText(values[i]);
        }
    }
}
