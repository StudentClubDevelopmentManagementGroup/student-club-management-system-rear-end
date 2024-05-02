package team.project.module.filestorage.internal.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.internal.config.LocalFileStorageConfig;
import team.project.module.filestorage.internal.dao.LocalFileStorageDAO;
import team.project.module.filestorage.internal.service.FileStorageBasicIService;
import team.project.module.filestorage.internal.util.Util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static team.project.module.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class LocalFileStorageService implements FileStorageBasicIService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;
    private final String baseUrl;

    @Autowired
    private LocalFileStorageDAO localFileStorageDAO;

    private LocalFileStorageService(LocalFileStorageConfig cfg) {
        this.uploadedFilesFolder  = cfg.uploadedFilesFolder;
        this.uploadedFileIdPrefix = cfg.uploadedFileIdPrefix;
        this.baseUrl = cfg.baseUrl;
    }

    /**
     * 由 filePath 生成 fileId
     * */
    private String generateFileId(String folderPath, String filename) {
        return Util.fixPath(uploadedFileIdPrefix + "/" + folderPath + "/" + filename);
    }

    /**
     * 从 fileId 解析出 filePath
     * */
    private String parseFileIdToFilePath(String fileId) {
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

        String filePath = parseFileIdToFilePath(fileId);
        if ( ! uploadFileQO.isOverwrite() && localFileStorageDAO.isFileExist(filePath)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            localFileStorageDAO.save(toUploadFile, filePath);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到本地文件系统时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }

    /**
     * 详见：{@link FileStorageBasicIService#getFileUrl}
     * */
    @Override
    public String getFileUrl(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return null;
        }
        if ( ! Util.isValidFileId(fileId)) {
            return null;
        }

        String filePath = parseFileIdToFilePath(fileId);
        String[] pathSplit = filePath.split("/");

        StringBuilder urlEncodedPath = new StringBuilder();
        for (int i = 1; i < pathSplit.length; i++) { /* <- i 从 1 开始，因为[0]是空字符串 "" */
            urlEncodedPath.append("/").append(URLEncoder.encode(pathSplit[i], StandardCharsets.UTF_8));
        }
        return baseUrl + urlEncodedPath;
    }

    /**
     * 详见：{@link FileStorageBasicIService#deleteFile}
     * */
    @Override
    public boolean deleteFile(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return true;
        }
        if ( ! Util.isValidFileId(fileId)) {
            return true;
        }
        try {
            String filePath = parseFileIdToFilePath(fileId);
            boolean ignored = localFileStorageDAO.delete(filePath);
            return true;
        }
        catch (Exception e) {
            log.error("从本地文件系统中删除文件时出现异常", e);
            return false;
        }
    }

    /* --------- */

    /**
     * （试验）
     * 将一段文本保存到 .txt 文件中
     * @param text         要保存的文本
     * @param uploadFileQO 详见 {@link UploadFileQO}（其中，必须要指定文件名）
     * */
    public String saveTextToFile(String text, UploadFileQO uploadFileQO) {

        if (uploadFileQO.isUsingOriginalFilename()) {
            throw new FileStorageException(INVALID_FILE_PATH, "未指定文件名");
        }

        String targetFolder = uploadFileQO.isTargetRootFolder() ? "/" : uploadFileQO.getTargetFolder();
        String targetFilename = uploadFileQO.getTargetFilename() + ".txt";

        String fileId = generateFileId(targetFolder, targetFilename);
        if ( ! Util.isValidFileId(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String filePath = parseFileIdToFilePath(fileId);
        if ( ! uploadFileQO.isOverwrite() && localFileStorageDAO.isFileExist(filePath)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            localFileStorageDAO.save(text, filePath);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到本地文件系统时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }
}
