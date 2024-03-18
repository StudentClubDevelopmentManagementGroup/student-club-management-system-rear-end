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
import team.project.module.filestorage.internal.config.FileStorageConfig;
import team.project.module.filestorage.internal.dao.AliyunOssStorageDAO;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;
import team.project.module.filestorage.internal.util.Util;

import java.io.IOException;

@Service
public class FileStorageService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LocalFileSystemDAO localFileSystemDAO;
    @Autowired
    AliyunOssStorageDAO aliyunOssStorageDAO;

    String uploadedFilesFolder;

    FileStorageService(FileStorageConfig cfg) {
        uploadedFilesFolder = cfg.uploadedFilesFolder;
    }

    public String uploadFileToLocalFileSystem(MultipartFile file) throws IOException {
        String newFilename = Util.generateRandomFileName(file.getOriginalFilename());
        localFileSystemDAO.save(uploadedFilesFolder, newFilename, file);
        return newFilename;
    }

    public String uploadFileToCloudStorage(MultipartFile file) throws IOException {
        String newFilename = Util.generateRandomFileName(file.getOriginalFilename());
        try {
            return aliyunOssStorageDAO.upload(uploadedFilesFolder, newFilename, file);
        } catch (OSSException | ClientException e) {
            logger.error("""
                上传文件到阿里云OSS的存储空间失败
                {}""", e.getMessage());
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "上传文件到云存储空间失败");
        }
    }

    public String getUploadedFileUrlFromCloudStorage(String fileId) {
        try {
            return aliyunOssStorageDAO.getUrl(fileId);
        } catch (OSSException | ClientException e) {
            logger.error("""
                从阿里云OSS的存储空间获取文件url失败
                {}""", e.getMessage());
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "从云存储空间失败获取url失败");
        }
    }
}
