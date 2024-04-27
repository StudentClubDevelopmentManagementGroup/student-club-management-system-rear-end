package team.project.module.filestorage.internal.service;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.internal.config.LocalFileSystemConfig;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;
import team.project.module.filestorage.internal.util.Util;

import static team.project.module.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class LocalFileSystemStorageService {
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
     * 生成 fileId
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
     * <p>  通过 fileId 判断文件是否可能存储在本地文件系统中</p>
     * <p>  如果文件存储在本地文件系统中，则 fileId 一定符合存储规则
     * <br> 操作文件前，先判断 fileId 是否符合这个规则
     * <br> 若不符合，则认为文件不存在，不必再进行后续操作</p>
     * */
    public boolean isValidFileId(String fileId) {
        return fileId.startsWith(uploadedFileIdPrefix + "/");
    }

    /**
     * 上传文件到指定目录
     * @param toUploadFile      要上传的文件
     * @param targetFolder      目标目录（路径分隔符用'/'，路径以'/'开头，根目录用"/"表示）（如果传 null 或 ""，则使用根目录）
     * @param targetFilename    目标文件名（不包括扩展名）（如果传 null 或 ""，则使用 {@code toUploadFile} 的原文件名）
     * @param overwrite         如果文件已存在，是否覆盖
     * @return fileId
     * @throws FileStorageException
     *      <li>如果文件已存在，且 {@code overwrite} 为 false，则 {@code status} 为 {@code FILE_EXIST}
     *      <li>如果上传途中遇到其他异常，则 {@code status} 为 {@code UNSOLVABLE}
     * */
    public String uploadFile(MultipartFile toUploadFile, String targetFolder, String targetFilename, boolean overwrite) {

        String filename;
        if (targetFilename == null || targetFilename.isEmpty()) {
            filename = toUploadFile.getOriginalFilename();
        } else {
            String extension = FilenameUtils.getExtension(toUploadFile.getOriginalFilename());
            filename = targetFilename + ("".equals(extension) ? "" : "." + extension);
        }

        String fileId = generateFileId(targetFolder, filename);
        String filePath = parseFileIdToFilePath(fileId);
        if ( ! overwrite && localFileSystemDAO.isFileExist(filePath)) {
            throw new FileStorageException(FILE_EXIST);
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
     * <p>通过 fileId 获取访问该文件的 URL</p>
     * <p>但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <li>文件不存在（此时访问该 URL 可能会返回 404）</li>
     * <li>...</li>
     * </p>
     * @return 如果 fileId 符合存储规则返回 URL，否则返回 null
     * */
    public String getUploadedFileUrl(String fileId) {
        if (isValidFileId(fileId))
            return baseUrl + parseFileIdToFilePath(fileId);
        else
            return null;
    }

    /**
     * 删除 fileId 指向的文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 删除成功返回 true，否则返回 false
     * */
    public boolean deleteUploadedFile(String fileId) {
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
