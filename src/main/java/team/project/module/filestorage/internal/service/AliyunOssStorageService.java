package team.project.module.filestorage.internal.service;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStoreException;
import team.project.module.filestorage.internal.config.AliyunOssConfig;
import team.project.module.filestorage.internal.dao.AliyunOssStorageDAO;
import team.project.module.filestorage.internal.util.Util;

import java.io.IOException;

import static team.project.module.filestorage.export.exception.FileStoreException.Status.FILE_EXIST;
import static team.project.module.filestorage.export.exception.FileStoreException.Status.UNSOLVABLE;

@Service
public class AliyunOssStorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;
    @Autowired
    private AliyunOssStorageDAO aliyunOssStorageDAO;

    AliyunOssStorageService(AliyunOssConfig cfg) {
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
     * <p>如果文件存储在阿里云 OSS 的存储空间中，则 fileId 一定符合存储规则<br>
     * 操作文件前，务必先判断 fileId 是否符合这个规则<br>
     * 若不符合，则认为文件不存在，不必再进行后续操作</p>
     * */
    public boolean isValidFileId(String fileId) {
        return fileId.startsWith(uploadedFileIdPrefix + "/");
    }

    /**
     * <p>上传文件到指定目录<br>
     * @param file 要上传的文件
     * @param targetFolder 目标目录（以'/'开头、以'/'开头分隔目录，根目录用"/"表示）
     * @param filename 文件名
     * @param overwrite 如果文件已存在，是否覆盖
     * @return fileId
     * */
    public String upload(MultipartFile file, String targetFolder, String filename, boolean overwrite) {
        String newFilename = file.getOriginalFilename();
        String fileId = generateFileId(targetFolder, filename);
        String fileKey = parseFileIdToFileKey(fileId);
        if ( ! overwrite && aliyunOssStorageDAO.isFileExist(fileKey)) {
            throw new FileStoreException(FILE_EXIST);
        }
        try {
            aliyunOssStorageDAO.upload(file, fileKey);

            return fileId;
        } catch (OSSException | ClientException | IOException e) {
            logger.error("上传文件到阿里云 OSS 的存储空间时出现异常", e);
            throw new FileStoreException(UNSOLVABLE);
        }
    }

    /**
     * 判断文件是否存在
     * */
    public boolean isFileExist(String fileId) {
        return isValidFileId(fileId) && aliyunOssStorageDAO.isFileExist(parseFileIdToFileKey(fileId));
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
        if ( ! isValidFileId(fileId)) {
            return null;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            return aliyunOssStorageDAO.getUrl(fileKey);
        } catch (OSSException | ClientException e) {
            logger.error("获取访问存储于阿里云 OSS 的存储空间中的文件的 url 时出现异常", e);
            return null;
        }
    }

    /**
     * 删除 fileId 指向的文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 执行时没有发生异常则返回 true，否则返回 false
     * */
    public boolean deleteUploadedFile(String fileId) {
        if ( ! isValidFileId(fileId)) {
            return true;
        }
        try {
            String fileKey = parseFileIdToFileKey(fileId);
            aliyunOssStorageDAO.delete(fileKey);
            return true;
        } catch (OSSException | ClientException e) {
            logger.error("从阿里云 OSS 的存储空间中删除文件时出现异常", e);
            return false;
        }
    }
}
