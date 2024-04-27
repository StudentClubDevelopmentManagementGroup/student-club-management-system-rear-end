package team.project.module.filestorage.export.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.AliyunObjectStorageService;
import team.project.module.filestorage.internal.service.LocalFileSystemStorageService;

@Service
public class FileStorageIServiceImpl implements FileStorageIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LocalFileSystemStorageService localStorageService;

    @Autowired
    AliyunObjectStorageService cloudStorageService;

    @Override
    public String uploadFile(MultipartFile toUploadFile, StorageType storageType, String targetFolder, String targetFilename, boolean overwrite) {
        return switch (storageType) {
            case LOCAL -> localStorageService.uploadFile(toUploadFile, targetFolder, targetFilename, overwrite);
            case CLOUD -> cloudStorageService.uploadFile(toUploadFile, targetFolder, targetFilename, overwrite);
        };
    }

    @Override
    public String getUploadedFileUrl(String fileId) {
        if (localStorageService.isValidFileId(fileId))
            return localStorageService.getUploadedFileUrl(fileId);

        if (cloudStorageService.isValidFileId(fileId))
            return cloudStorageService.getUploadedFileUrl(fileId);

        return null;
    }

    @Override
    public boolean deleteUploadedFile(String fileId) {
        if (localStorageService.isValidFileId(fileId))
            return localStorageService.deleteUploadedFile(fileId);

        if (cloudStorageService.isValidFileId(fileId))
            return cloudStorageService.deleteUploadedFile(fileId);

        return true;
    }
}
