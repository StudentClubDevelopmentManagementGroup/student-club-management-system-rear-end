package team.project.module.filestorage.internal.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.config.LocalFileSystemConfig;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;
import team.project.module.filestorage.internal.util.Util;

@Service
public class LocalFileSystemService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;
    private final String baseUrl;

    @Autowired
    private LocalFileSystemDAO localFileSystemDAO;

    LocalFileSystemService(LocalFileSystemConfig cfg) {
        this.uploadedFilesFolder = cfg.uploadedFilesFolder;
        this.uploadedFileIdPrefix = cfg.uploadedFileIdPrefix;
        this.baseUrl = cfg.baseUrl;
    }

    /**
     * <p>通过 fileId 判断文件是否可能存储在本地文件系统中</p>
     * <p>如果文件存储在本地文件系统中，则 fileId 一定符合某种规则<br>
     * 操作文件前，先判断 fileId 是否符合这个规则<br>
     * 若不符合，则认为文件不存在，不必再进行后续操作</p>
     * */
    public boolean isValidFileId(String fileId) {
        return fileId.startsWith(uploadedFileIdPrefix);
    }

    /**
     * 上传文件（文件会更名，存储在特定目录下）
     * @return 如果成功返回 fileId，如果上传途中出现异常则返回 null
     * */
    public String upload(MultipartFile file) {
        String newFilename = Util.generateRandomFileName(file.getOriginalFilename());

        String fileId = uploadedFileIdPrefix + "/" + newFilename;

        try {
            localFileSystemDAO.save(uploadedFilesFolder, newFilename, file);
            return fileId;
        }
        catch (Exception e) {
            logger.error("上传文件到本地文件系统时出现异常", e);
            return null;
        }
    }

    /**
     * <p>通过 fileId 获取访问该文件的 URL</p>
     * <p>但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <li>文件不存在（此时访问该 URL 可能会返回 404）</li>
     * <li>...</li>
     * </p>
     * @return 如果获取成功，则返回 URL
     * */
    public String getUploadedFileUrl(String fileId) {
        String filePath = uploadedFilesFolder + fileId.substring(uploadedFileIdPrefix.length());
        return baseUrl + filePath;
    }

    /**
     * 删除文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 执行时没有发生异常则返回 true，否则返回 false
     * */
    public boolean deleteUploadedFile(String fileId) {
        String fileName = fileId.substring(uploadedFileIdPrefix.length() + 1);
        try {
            boolean ignored = localFileSystemDAO.delete(uploadedFilesFolder, fileName);
            return true;
        }
        catch (Exception e) {
            logger.error("从本地文件系统中删除文件时出现异常", e);
            return false;
        }
    }
}
