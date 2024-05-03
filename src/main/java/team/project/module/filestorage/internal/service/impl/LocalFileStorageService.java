package team.project.module.filestorage.internal.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.internal.config.LocalFileStorageConfig;
import team.project.module.filestorage.internal.dao.LocalFileStorageDAO;
import team.project.module.filestorage.internal.service.FileStorageBasicIService;
import team.project.module.filestorage.internal.service.TextFileStorageIService;
import team.project.module.filestorage.internal.util.Util;

import java.io.IOException;

import static team.project.module.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class LocalFileStorageService implements FileStorageBasicIService, TextFileStorageIService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;

    @Autowired
    private LocalFileStorageDAO localFileStorageDAO;

    private LocalFileStorageService(LocalFileStorageConfig cfg) {
        this.uploadedFilesFolder  = cfg.uploadedFilesFolder;
        this.uploadedFileIdPrefix = cfg.uploadedFileIdPrefix;
    }

    /**
     * 由 filePath 生成 fileId
     * */
    private String generateFileId(String folderPath, String filename) {
        return Util.fixSeparator(uploadedFileIdPrefix + "/" + folderPath + "/" + filename);
    }

    /**
     * 从 fileId 解析出 filePath
     * */
    private String parseFileIdToFilePath(String fileId) {
        return uploadedFilesFolder + fileId.substring(uploadedFileIdPrefix.length());
    }

    /* -- 基本操作（上传、获取 url、删除） -- */

    /**
     * 详见：{@link FileStorageBasicIService#uploadFile}
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
        if ( ! Util.isValidFileId(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String filePath = parseFileIdToFilePath(fileId);
        if ( ! uploadFileQO.isOverwrite() && localFileStorageDAO.isFileExist(filePath)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            localFileStorageDAO.saveFile(toUploadFile, filePath);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到本地文件系统时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }

    /**
     * 详见：{@link FileStorageBasicIService#mayBeStored}
     * */
    @Override
    public boolean mayBeStored(String fileId) {
        return fileId.startsWith(uploadedFileIdPrefix + "/") && Util.isValidFileId(fileId);
    }

    /**
     * 详见：{@link FileStorageBasicIService#getFileUrl}
     * */
    @Override
    public String getFileUrl(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return null;
        }
        String filePath = parseFileIdToFilePath(fileId);
        return localFileStorageDAO.getFileUrl(filePath);
    }

    /**
     * 详见：{@link FileStorageBasicIService#deleteFile}
     * */
    @Override
    public boolean deleteFile(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return true;
        }
        try {
            String filePath = parseFileIdToFilePath(fileId);
            boolean ignored = localFileStorageDAO.deleteFile(filePath);
            return true;
        }
        catch (Exception e) {
            log.error("从本地文件系统中删除文件时出现异常", e);
            return false;
        }
    }

    /* -- 读写纯文本文件 -- */

    /**
     * 详见：{@link TextFileStorageIService#uploadTextToFile}
     * */
    @Override
    public String uploadTextToFile(String text, UploadFileQO uploadFileQO) {

        if (StringUtils.isBlank(uploadFileQO.getTargetFilename())) {
            throw new FileStorageException(INVALID_FILE_PATH, "未指定文件名");
        }

        String targetFolder   = StringUtils.isBlank(uploadFileQO.getTargetFolder()) ? "/" : uploadFileQO.getTargetFolder();
        String targetFilename = uploadFileQO.getTargetFilename();

        String fileId = generateFileId(targetFolder, targetFilename);
        if ( ! Util.isValidFileId(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String filePath = parseFileIdToFilePath(fileId);
        if ( ! uploadFileQO.isOverwrite() && localFileStorageDAO.isFileExist(filePath)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            localFileStorageDAO.saveTextToFile(text, filePath);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到本地文件系统时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }

    /**
     * 详见：{@link TextFileStorageIService#readTextFromFile}
     * */
    @Override
    public String readTextFromFile(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return null;
        }
        try {
            String filePath = parseFileIdToFilePath(fileId);
            return localFileStorageDAO.readTextFromFile(filePath);
        } catch (IOException e) {
            log.error("从本地文件系统中读取文件时出现异常", e);
            return null;
        }
    }
}
