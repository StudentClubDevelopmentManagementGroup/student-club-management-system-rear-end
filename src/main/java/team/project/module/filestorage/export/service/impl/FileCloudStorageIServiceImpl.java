package team.project.module.filestorage.export.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.AliyunOssStorageService;

@Service("file-cloud-storage")
public class FileCloudStorageIServiceImpl implements FileStorageIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AliyunOssStorageService aliyunOssStorageService;

    @Override
    public String uploadFile(MultipartFile file, String targetFolder, String filename, boolean overwrite) {
        return aliyunOssStorageService.upload(file, targetFolder, filename, overwrite);
    }

    @Override
    public boolean isFileExist(String fileId) {
        return aliyunOssStorageService.isFileExist(fileId);
    }

    @Override
    public String getUploadedFileUrl(String fileId) {
        return aliyunOssStorageService.getUploadedFileUrl(fileId);
    }

    @Override
    public boolean deleteUploadedFile(String fileId) {
        return aliyunOssStorageService.deleteUploadedFile(fileId);
    }
}
