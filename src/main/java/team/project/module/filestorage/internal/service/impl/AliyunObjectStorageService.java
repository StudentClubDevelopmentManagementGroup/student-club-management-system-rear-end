package team.project.module.filestorage.internal.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.internal.config.AliyunOssConfig;
import team.project.module.filestorage.internal.dao.AliyunOssDAO;
import team.project.module.filestorage.internal.service.FileStorageBasicIService;
import team.project.module.filestorage.internal.util.Util;

import static team.project.module.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class AliyunObjectStorageService implements FileStorageBasicIService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;

    @Autowired
    private AliyunOssDAO aliyunOssDAO;

    AliyunObjectStorageService(AliyunOssConfig cfg) {
        this.uploadedFilesFolder = cfg.uploadedFilesFolder;
        this.uploadedFileIdPrefix = cfg.uploadedFileIdPrefix;
    }

    /**
     * 生成 fileId
     * */
    private String generateFileId(String folderPath, String filename) {
        return Util.fixPath(uploadedFileIdPrefix + "/" + folderPath + "/" + filename);
    }

    /**
     * 从 fileId 解析出 fileKey
     * */
    private String parseFileIdToFileKey(String fileId) {
        return uploadedFilesFolder + fileId.substring(uploadedFileIdPrefix.length());
    }

    /**
     * 详见：{@link FileStorageBasicIService#mayBeStored}
     * */
    @Override
    public boolean mayBeStored(String fileId) {
        return fileId.startsWith(uploadedFileIdPrefix + "/");
    }

    /**
     * 详见：{@link FileStorageBasicIService#uploadFile}
     * */
    @Override
    public String uploadFile(MultipartFile toUploadFile, UploadFileQO uploadFileQO) {

        String targetFolder = uploadFileQO.isTargetRootFolder() ? "/" : uploadFileQO.getTargetFolder();

        String targetFilename;
        if (uploadFileQO.isUsingOriginalFilename()) {
            targetFilename = toUploadFile.getOriginalFilename();
        } else {
            String extension = FilenameUtils.getExtension(toUploadFile.getOriginalFilename());
            targetFilename = uploadFileQO.getTargetFilename() + ("".equals(extension) ? "" : "." + extension);
        }

        String fileId = generateFileId(targetFolder, targetFilename);
        if ( ! Util.isValidFileId(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String fileKey = parseFileIdToFileKey(fileId);
        if ( ! uploadFileQO.isOverwrite() && aliyunOssDAO.isFileExist(fileKey)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            aliyunOssDAO.upload(toUploadFile, fileKey);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到阿里云 OSS 的存储空间时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }

    /**
     * 详见：{@link FileStorageBasicIService#getUploadedFileUrl}
     * */
    @Override
    public String getUploadedFileUrl(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return null;
        }
        if ( ! Util.isValidFileId(fileId)) {
            return null;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            return aliyunOssDAO.getUrl(fileKey);
        }
        catch (Exception e) {
            log.error("获取访问存储于阿里云 OSS 的存储空间中的文件的 url 时出现异常", e);
            return null;
        }
    }

    /**
     * 详见：{@link FileStorageBasicIService#deleteUploadedFile}
     * */
    @Override
    public boolean deleteUploadedFile(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return true;
        }
        if ( ! Util.isValidFileId(fileId)) {
            return true;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            aliyunOssDAO.delete(fileKey);
            return true;
        }
        catch (Exception e) {
            log.error("从阿里云 OSS 的存储空间中删除文件时出现异常", e);
            return false;
        }
    }
}
