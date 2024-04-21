package team.project.module.filestorage.internal.service;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.internal.config.AliyunOssConfig;
import team.project.module.filestorage.internal.dao.AliyunOssStorageDAO;
import team.project.module.filestorage.internal.util.Util;

import java.io.IOException;

@Service
public class AliyunOssStorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;
    @Autowired
    private AliyunOssStorageDAO aliyunOssStorageDAO;

    AliyunOssStorageService(AliyunOssConfig cfg) {
        this.uploadedFilesFolder = cfg.uploadedFilesFolder;
        this.uploadedFileIdPrefix = cfg.uploadedFileIdPrefix;
    }

    public String upload(MultipartFile file) throws IOException {
        String newFilename = Util.generateRandomFileName(file.getOriginalFilename());

        String fileId = uploadedFileIdPrefix + "/" + newFilename;
        String fileKey = uploadedFilesFolder + "/" + newFilename;

        try {
            aliyunOssStorageDAO.upload(fileKey, file);

            return fileId;
        } catch (OSSException | ClientException e) {
            logger.error("上传文件到阿里云OSS的存储空间失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "上传文件到云存储空间失败");
        }
    }

    public String getUploadedFileUrl(String fileId) {
        if ( ! fileId.startsWith(uploadedFileIdPrefix)) {
            return null;
        }
        String fileKey = uploadedFilesFolder + fileId.substring(uploadedFileIdPrefix.length());
        try {
            return aliyunOssStorageDAO.getUrl(fileKey);
        } catch (OSSException | ClientException e) {
            logger.error("从阿里云OSS的存储空间获取访问文件的url失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "从云存储空间失败获取url失败");
        }
    }

    public boolean deleteUploadedFile(String fileId) {
        if ( ! fileId.startsWith(uploadedFileIdPrefix)) {
            return false;
        }
        try {
            String fileKey = uploadedFilesFolder + "/" + fileId.substring(uploadedFileIdPrefix.length());
            aliyunOssStorageDAO.delete(fileKey);
            return true;
        } catch (OSSException | ClientException e) {
            logger.error("从阿里云OSS的存储空间删除文件失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "从云存储空间删除文件失败");
        }
    }
}
