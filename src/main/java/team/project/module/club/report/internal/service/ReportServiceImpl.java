package team.project.module.club.report.internal.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.report.internal.mapper.TblReportMapper;
import team.project.module.club.report.internal.model.entity.TblReport;
import team.project.module.club.report.internal.model.view.ReportInfoVO;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;

import java.util.ArrayList;
import java.util.List;

import static team.project.module.util.filestorage.export.model.enums.FileStorageType.CLOUD;
@Service
public class ReportServiceImpl extends ServiceImpl<TblReportMapper, TblReport> implements ReportService {
    @Autowired
    TblReportMapper reportMapper;

    @Autowired
    FileStorageServiceI fileStorageServiceI;

    @Autowired
    AuthServiceI authService;

    @Override
    public List<String> createReport(String uploader, Long clubId, MultipartFile[] reportFileList, String reportType) {

        JsonObject jsonObjects = new JsonObject();

//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String uploadFileBasePath = "/report/" + uploader + "/" + reportType + "/" ;
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
                    String fileId = fileStorageServiceI.uploadFile(file, CLOUD, uploadFileQO);
                    fileIds.add(fileId);
                    // 添加到 JSON 数组
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("file_name", fileName);
                    jsonObject.addProperty("fileId", fileId);

                    jsonObjects.add("file"+ time,jsonObject);
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
        String arrangerId = (String)( StpUtil.getLoginId() );
        if(!reportMapper.getReportUploader(reportId).equals(arrangerId)){
            authService.requireSuperAdmin(arrangerId, "除了超级管理员，只有社团成员能删除自己的成果汇报");
        }
        return baseMapper.deleteReport(reportId,clubId);
    }

    @Override
    public List<String> updateReport(String uploader,Long reportId, Long clubId, MultipartFile[] reportFileList, String reportType) {
        String arrangerId = (String)( StpUtil.getLoginId() );
        if(!reportMapper.getReportUploader(reportId).equals(arrangerId)){
            authService.requireSuperAdmin(arrangerId, "除了超级管理员，只有社团成员能删除自己的成果汇报");
        }
        JsonObject jsonObjects = new JsonObject();
        String uploadFileBasePath = "/report/" + uploader + "/" + reportType + "/" ;
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
                    String fileId = fileStorageServiceI.uploadFile(file, CLOUD, uploadFileQO);
                    fileIds.add(fileId);
                    // 添加到 JSON 数组
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("file_name", fileName);
                    jsonObject.addProperty("fileId", fileId);

                    jsonObjects.add("file"+ time,jsonObject);
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
    public PageVO<ReportInfoVO> getReportList(Page<Object> Page, Long clubId) {
        Page<TblReport> page = reportMapper.getReportList(Page, clubId);
        List<ReportInfoVO> reportInfoVOList = new ArrayList<>();
        for(TblReport tblReport : page.getRecords()){
            ReportInfoVO reportInfoVO = new ReportInfoVO();
            reportInfoVO.setId(tblReport.getId());
            reportInfoVO.setReportType(tblReport.getReportType());
            reportInfoVO.setUploader(tblReport.getUploader());
            reportInfoVO.setCreateTime(tblReport.getCreateTime());
            reportInfoVO.setUpdateTime(tblReport.getUpdateTime());
            reportInfoVO.setDeleted(tblReport.getDeleted());
            if(tblReport.getReportFileList() == null)
            {
                reportInfoVO.setReportFile(null);
            }
            else{
                String[] fileIdArray = tblReport.getReportFileList().toString().split(",");
                List<String> fileUrlList = new ArrayList<>();
                for (String fileId : fileIdArray) {
                    if (StringUtils.isNotBlank(fileId)) {
                        String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                        fileUrlList.add(fileUrl);
                    }
                }
                reportInfoVO.setReportFile(fileUrlList);
            }
            reportInfoVOList.add(reportInfoVO);
        }
        if (page.getTotal() == 0) {
            return null;
        } else {
            return new PageVO<>(reportInfoVOList, new Page<>(Page.getPages(), Page.getSize(), page.getTotal()));
        }
    }
}
