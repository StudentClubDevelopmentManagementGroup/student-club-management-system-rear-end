package team.project.module.filestorage.export.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageIService {

    /**
     * 上传文件到本地文件系统
     * @return 如果上传成功，返回文件id
     * */
    String uploadFileToLocalFileSystem(MultipartFile file) throws IOException;

    /**
     * 上传文件到云存储空间
     * @return 如果上传成功，返回文件id
     * */
    String uploadFileToCloudStorage(MultipartFile file) throws IOException;

    /**
     * 通过文件id获取访问该文件的 URL
     * @return 获取访问该文件的 URL（fileId 指向的文件不存在也会返回 URL，访问这个 URL 会响应文件不存在）
     * */
    String getUploadedFileUrl(String fileId);

    /**
     * 删除已上传的文件
     * */
    void deleteUploadedFile(String fileId);
}
