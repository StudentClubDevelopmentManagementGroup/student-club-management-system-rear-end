package team.project.module.util.filestorage.internal.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.internal.config.AliyunOssConfig;
import team.project.module.util.filestorage.internal.dao.AliyunOssDAO;
import team.project.module.util.filestorage.internal.service.FileStorageBasicServiceI;
import team.project.module.util.filestorage.internal.service.TextFileStorageServiceI;
import team.project.module.util.filestorage.internal.util.Util;

import static team.project.module.util.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class AliyunObjectStorageService implements FileStorageBasicServiceI, TextFileStorageServiceI {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;

    @Autowired
    private AliyunOssDAO aliyunOssDAO;

    AliyunObjectStorageService(AliyunOssConfig cfg) {
        this.uploadedFilesFolder  = cfg.uploadedFilesFolder;
        this.uploadedFileIdPrefix = cfg.uploadedFileIdPrefix;
    }

    /**
     * 依据文件路径生成 fileId
     * */
    private String generateFileId(String folderPath, String filename) {
        return Util.fixSeparator(uploadedFileIdPrefix + "/" + folderPath + "/" + filename);
    }

    /**
     * 判断 fileId 是否一定无效
     * */
    private boolean isFileIdNotValid(String fileId) {
        return Util.hasInvalidChar(fileId) || Util.hasRelativePathPart(fileId);
    }

    /**
     * 从 fileId 解析出 fileKey（解析前先确保 fileId 的格式正确）
     * */
    private String parseFileIdToFileKey(String fileId) {
        return uploadedFilesFolder + fileId.substring(uploadedFileIdPrefix.length());
    }

    /* -- 基本操作（上传、获取 url、删除） -- */

    /**
     * 详见：{@link FileStorageBasicServiceI#uploadFile}
     * */
    @Override
    public String uploadFile(MultipartFile toUploadFile, UploadFileQO uploadFileQO) {

        String targetFolder   = StringUtils.isBlank(uploadFileQO.getTargetFolder())
                                  ? "/"
                                  : uploadFileQO.getTargetFolder();
        String targetFilename = StringUtils.isBlank(uploadFileQO.getTargetFilename())
                                  ? toUploadFile.getOriginalFilename()
                                  : uploadFileQO.getTargetFilename();

        String fileId = generateFileId(targetFolder, targetFilename);
        if (isFileIdNotValid(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String fileKey = parseFileIdToFileKey(fileId);
        if ( ! uploadFileQO.isOverwrite() && aliyunOssDAO.isFileExist(fileKey)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            aliyunOssDAO.uploadFile(toUploadFile, fileKey);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到阿里云 OSS 的存储空间时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }

    /**
     * 详见：{@link FileStorageBasicServiceI#mayBeStored}
     * */
    @Override
    public boolean mayBeStored(String fileId) {
        return fileId.startsWith(uploadedFileIdPrefix + "/") && ! isFileIdNotValid(fileId);
    }

    /**
     * 详见：{@link FileStorageBasicServiceI#getFileUrl}
     * */
    @Override
    public String getFileUrl(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return null;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            return aliyunOssDAO.getFileUrl(fileKey);
        }
        catch (Exception e) {
            log.error("获取访问存储于阿里云 OSS 的存储空间中的文件的 url 时出现异常", e);
            return null;
        }
    }

    /**
     * 详见：{@link FileStorageBasicServiceI#deleteFile}
     * */
    @Override
    public boolean deleteFile(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return true;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            aliyunOssDAO.deleteFile(fileKey);
            return true;
        }
        catch (Exception e) {
            log.error("从阿里云 OSS 的存储空间中删除文件时出现异常", e);
            return false;
        }
    }

    /* -- 读写纯文本文件 -- */

    /**
     * 详见：{@link TextFileStorageServiceI#uploadTextToFile}
     * */
    @Override
    public String uploadTextToFile(String text, UploadFileQO uploadFileQO) {

        if (StringUtils.isBlank(uploadFileQO.getTargetFilename())) {
            throw new FileStorageException(INVALID_FILE_PATH, "未指定文件名");
        }

        String targetFolder   = StringUtils.isBlank(uploadFileQO.getTargetFolder()) ? "/" : uploadFileQO.getTargetFolder();
        String targetFilename = uploadFileQO.getTargetFilename();

        String fileId = generateFileId(targetFolder, targetFilename);
        if (isFileIdNotValid(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String fileKey = parseFileIdToFileKey(fileId);
        if ( ! uploadFileQO.isOverwrite() && aliyunOssDAO.isFileExist(fileKey)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            aliyunOssDAO.uploadTextToFile(text, fileKey);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到阿里云 OSS 的存储空间时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }

    /**
     * 如果文件不是纯文本文件，或是编码不匹配，会读取出乱码文本
     * 其他介绍详见：{@link TextFileStorageServiceI#getTextFromFile}
     * */
    @Override
    public String getTextFromFile(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return null;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            return aliyunOssDAO.readTextFromFile(fileKey);
        }
        catch (Exception e) {
            log.error("从阿里云 OSS 的存储空间中读取文件时出现异常", e);
            return null;
        }
    }
}
