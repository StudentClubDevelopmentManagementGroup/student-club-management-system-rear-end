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
     * @return 获取访问该文件的 URL
     * */
    String getUploadedFileUrl(String fileId);
}
