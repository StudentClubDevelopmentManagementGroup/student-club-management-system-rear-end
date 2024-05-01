package team.project.module.filestorage.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.impl.AliyunObjectStorageService;
import team.project.module.filestorage.internal.service.impl.LocalFileSystemStorageService;

@Service
public class FileStorageIServiceImpl implements FileStorageIService {

    @Autowired
    LocalFileSystemStorageService localStorageService;

    @Autowired
    AliyunObjectStorageService cloudStorageService;

    /**
     *  详见：{@link FileStorageIService#uploadFile}
     * */
    @Override
    public String uploadFile(MultipartFile toUploadFile, StorageType storageType, String targetFolder, String targetFilename, boolean overwrite) {
        return switch (storageType) {
            case LOCAL -> localStorageService.uploadFile(toUploadFile, targetFolder, targetFilename, overwrite);
            case CLOUD -> cloudStorageService.uploadFile(toUploadFile, targetFolder, targetFilename, overwrite);
        };
    }

    /**
     *  详见：{@link FileStorageIService#getUploadedFileUrl}
     * */
    @Override
    public String getUploadedFileUrl(String fileId) {
        if (localStorageService.mayBeStored(fileId))
            return localStorageService.getUploadedFileUrl(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.getUploadedFileUrl(fileId);

        return null;
    }

    /**
     *  详见：{@link FileStorageIService#deleteUploadedFile}
     * */
    @Override
    public boolean deleteUploadedFile(String fileId) {
        if (localStorageService.mayBeStored(fileId))
            return localStorageService.deleteUploadedFile(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.deleteUploadedFile(fileId);

        return true;
    }
}
