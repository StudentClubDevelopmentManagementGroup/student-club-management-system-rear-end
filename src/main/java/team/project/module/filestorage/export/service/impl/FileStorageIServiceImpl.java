package team.project.module.filestorage.export.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.AliyunOssStorageService;
import team.project.module.filestorage.internal.service.LocalFileSystemService;

import java.io.IOException;

@Service
public class FileStorageIServiceImpl implements FileStorageIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LocalFileSystemService localFileSystemService;

    @Autowired
    private AliyunOssStorageService aliyunOssStorageService;

    @Override
    public String uploadFileToLocalFileSystem(MultipartFile file) throws IOException {
        return localFileSystemService.upload(file);
    }

    @Override
    public String uploadFileToCloudStorage(MultipartFile file) throws IOException {
        return aliyunOssStorageService.upload(file);
    }

    @Override
    public String getUploadedFileUrl(String fileId) {
        String url;
        url = aliyunOssStorageService.getUploadedFileUrl(fileId);
        if (null != url) {
            return url;
        }
        url = localFileSystemService.getUploadedFileUrl(fileId);
        if (null != url) {
            return url;
        }
        return null;
    }

    @Override
    public void deleteUploadedFile(String fileId) {

    }
}
