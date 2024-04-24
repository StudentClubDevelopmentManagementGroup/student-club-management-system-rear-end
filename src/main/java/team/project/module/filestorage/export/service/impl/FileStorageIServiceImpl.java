package team.project.module.filestorage.export.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.AliyunOssStorageService;
import team.project.module.filestorage.internal.service.LocalFileSystemService;

@Service
public class FileStorageIServiceImpl implements FileStorageIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LocalFileSystemService localFileSystemService;

    @Autowired
    private AliyunOssStorageService aliyunOssStorageService;

    @Override
    public String uploadFileToLocalFileSystem(MultipartFile file, String targetFolder) {
        return localFileSystemService.upload(file, targetFolder);
    }

    @Override
    public String uploadFileToCloudStorage(MultipartFile file, String targetFolder) {
        return aliyunOssStorageService.upload(file, targetFolder);
    }

    @Override
    public String getUploadedFileUrl(String fileId) {
        if (aliyunOssStorageService.isValidFileId(fileId)) {
            return aliyunOssStorageService.getUploadedFileUrl(fileId);
        }
        else if (localFileSystemService.isValidFileId(fileId)) {
            return localFileSystemService.getUploadedFileUrl(fileId);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean deleteUploadedFile(String fileId) {
        if (aliyunOssStorageService.isValidFileId(fileId)) {
            return aliyunOssStorageService.deleteUploadedFile(fileId);
        }
        else if (localFileSystemService.isValidFileId(fileId)) {
            return localFileSystemService.deleteUploadedFile(fileId);
        }
        else {
            return false;
        }
    }
}
