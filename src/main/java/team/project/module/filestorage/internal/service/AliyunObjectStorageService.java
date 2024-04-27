package team.project.module.filestorage.internal.service;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.internal.config.AliyunOssConfig;
import team.project.module.filestorage.internal.dao.AliyunOssDAO;
import team.project.module.filestorage.internal.util.Util;

import java.io.IOException;

import static team.project.module.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class AliyunObjectStorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
     * <p>通过 fileId 判断文件是否可能存储在阿里云 OSS 的存储空间中</p>
     * <p>  如果文件存储在阿里云 OSS 的存储空间中，则 fileId 一定符合存储规则
     * <br> 操作文件前，先判断 fileId 是否符合这个规则
     * <br> 若不符合，则认为文件不存在，不必再进行后续操作</p>
     * */
    public boolean maybeStoredInAliyunOSS(String fileId) {
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
     *      <li>目标目录路径或目标文件名不合约束
     *      <li>如果文件已存在，且 {@code overwrite} 为 false
     *      <li>或是上传途中遇到其他异常
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
        if ( ! Util.isValidFileId(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String fileKey = parseFileIdToFileKey(fileId);
        if ( ! overwrite && aliyunOssDAO.isFileExist(fileKey)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }
        try {
            aliyunOssDAO.upload(toUploadFile, fileKey);

            return fileId;
        } catch (OSSException | ClientException | IOException e) {
            logger.error("上传文件到阿里云 OSS 的存储空间时出现异常", e);
            throw new FileStorageException(UNSOLVABLE);
        }
    }

    /**
     * <p>通过 fileId 获取访问该文件的 URL</p>
     * <p>但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <li>文件不存在（此时访问该 URL 可能会返回 404）</li>
     * <li>URL 设置了有效访问时长，访问时可能已经过期</li>
     * <li>...</li>
     * </p>
     * @return 如果 fileId 符合存储规则返回 URL，否则返回 null
     * */
    public String getUploadedFileUrl(String fileId) {
        if ( ! maybeStoredInAliyunOSS(fileId)) {
            return null;
        }
        if ( ! Util.isValidFileId(fileId)) {
            return null;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            return aliyunOssDAO.getUrl(fileKey);
        } catch (OSSException | ClientException e) {
            logger.error("获取访问存储于阿里云 OSS 的存储空间中的文件的 url 时出现异常", e);
            return null;
        }
    }

    /**
     * 删除 fileId 指向的文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 删除成功返回 true，否则返回 false
     * */
    public boolean deleteUploadedFile(String fileId) {
        if ( ! maybeStoredInAliyunOSS(fileId)) {
            return true;
        }
        if ( ! Util.isValidFileId(fileId)) {
            return true;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            aliyunOssDAO.delete(fileKey);
            return true;
        } catch (OSSException | ClientException e) {
            logger.error("从阿里云 OSS 的存储空间中删除文件时出现异常", e);
            return false;
        }
    }
}
