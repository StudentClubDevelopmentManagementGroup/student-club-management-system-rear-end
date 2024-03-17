package team.project.module.filestorage.internal.service;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.internal.config.FileStorageConfig;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;

import java.util.UUID;

@Service
public class FileStorageService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LocalFileSystemDAO localFileSystemDAO;

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
}
