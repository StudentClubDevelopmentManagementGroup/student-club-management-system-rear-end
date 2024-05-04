package team.project.module.filestorage.internal.service.impl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.internal.config.LocalFileSystemConfig;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;
import team.project.module.filestorage.internal.service.FileStorageAService;
import team.project.module.filestorage.internal.util.Util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static team.project.module.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class LocalFileSystemStorageService extends FileStorageAService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;
    private final String baseUrl;

    @Autowired
    private LocalFileSystemDAO localFileSystemDAO;

    LocalFileSystemStorageService(LocalFileSystemConfig cfg) {
        this.uploadedFilesFolder = cfg.uploadedFilesFolder;
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
     * 详见：{@link FileStorageAService#mayBeStored}
     * */
    @Override
    public boolean mayBeStored(String fileId) {
        return fileId.startsWith(uploadedFileIdPrefix + "/");
    }

    /**
     * 详见：{@link FileStorageAService#uploadFile}
     * */
    @Override
    public String uploadFile(MultipartFile toUploadFile, String targetFolder, String targetFilename, boolean overwrite) {

        String folder = (targetFolder == null || targetFolder.isEmpty()) ? "/" : targetFolder;

        String filename;
        if (targetFilename == null || targetFilename.isEmpty()) {
            filename = toUploadFile.getOriginalFilename();
        } else {
            String extension = FilenameUtils.getExtension(toUploadFile.getOriginalFilename());
            filename = targetFilename + ("".equals(extension) ? "" : "." + extension);
        }

        String fileId = generateFileId(folder, filename);
        if ( ! isValidFileId(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String filePath = parseFileIdToFilePath(fileId);

        if ( ! overwrite && localFileSystemDAO.isFileExist(filePath)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            localFileSystemDAO.save(toUploadFile, filePath);
            return fileId;
        }
        catch (Exception e) {
            logger.error("上传文件到本地文件系统时出现异常", e);
            throw new FileStorageException(UNSOLVABLE);
        }
    }

    /**
     * 详见：{@link FileStorageAService#getUploadedFileUrl}
     * */
    @Override
    public String getUploadedFileUrl(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return null;
        }
        if ( ! isValidFileId(fileId)) {
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
     * 详见：{@link FileStorageAService#deleteUploadedFile}
     * */
    @Override
    public boolean deleteUploadedFile(String fileId) {
        if ( ! mayBeStored(fileId)) {
            return true;
        }
        if ( ! isValidFileId(fileId)) {
            return true;
        }
        try {
            String filePath = parseFileIdToFilePath(fileId);
            boolean ignored = localFileSystemDAO.delete(filePath);
            return true;
        }
        catch (Exception e) {
            logger.error("从本地文件系统中删除文件时出现异常", e);
            return false;
        }
    }
}
