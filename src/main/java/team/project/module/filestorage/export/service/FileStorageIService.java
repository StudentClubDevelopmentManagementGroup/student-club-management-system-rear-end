package team.project.module.filestorage.export.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageIService {

    String uploadFileToLocalFileSystem(MultipartFile file) throws IOException;

    String uploadFileToCloudStorage(MultipartFile file) throws IOException;

    String getUploadedFileUrl(String fileId);
}
