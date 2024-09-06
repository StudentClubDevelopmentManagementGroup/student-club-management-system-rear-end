package team.project.module.club.report.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.report.internal.mapper.TblReportMapper;
import team.project.module.club.report.internal.model.entity.TblReport;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static team.project.module.util.filestorage.export.model.enums.FileStorageType.CLOUD;
@Service
public class ReportServiceImpl extends ServiceImpl<TblReportMapper, TblReport> implements ReportService {
    @Autowired
    TblReportMapper reportMapper;

    @Autowired
    FileStorageServiceI fileStorageServiceI;

    @Override
    public List<String> createReport(String uploader, Long clubId, MultipartFile[] reportFileList, String reportType) {

        JsonObject jsonObjects = new JsonObject();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
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

        // 将所有文件ID以逗号分隔后存储
        String allFileIds = String.join(",", fileIds);
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
    public int deleteReport(Long id) {
        return baseMapper.deleteReport(id);
    }



    public boolean isValidJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
