package team.project.module.filestorage.internal.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import org.apache.commons.io.FilenameUtils;
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

import java.util.UUID;

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

    public String uploadFileToLocalFileSystem(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFileName);
        String targetFolder = uploadedFilesFolder;
        String newFileName = UUID.randomUUID().toString() + ("".equals(extension) ? "" : ".") + extension;

        try {
            localFileSystemDAO.write(targetFolder, newFileName, file);
            return newFileName;
        } catch (Exception e) {
            String errStr = "上传文件到服务器本地文件系统失败";
            logger.error(errStr, e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, errStr);
        }
    }

    public String uploadFileToAliyunOssBucket(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFileName);
        String targetFolder = "/test/";
        String newFileName = UUID.randomUUID().toString() + ("".equals(extension) ? "" : ".") + extension;

        String targetFilepath = targetFolder + "/" + newFileName;

        try {
            aliyunOssStorageDAO.upload(targetFilepath, file);
            return newFileName;

        } catch (OSSException | ClientException e) {
            logger.error("""
                上传文件到阿里云OSS的存储空间失败
                {}""", e.getMessage());
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "上传文件到阿里云OSS的存储空间失败");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "上传文件到阿里云OSS的存储空间失败");
        }
    }
}
