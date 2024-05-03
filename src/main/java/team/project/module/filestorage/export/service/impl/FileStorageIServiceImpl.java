package team.project.module.filestorage.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.impl.AliyunObjectStorageService;
import team.project.module.filestorage.internal.service.impl.LocalFileStorageService;

@Service
public class FileStorageIServiceImpl implements FileStorageIService {

    @Autowired
    LocalFileStorageService localStorageService;

    @Autowired
    AliyunObjectStorageService cloudStorageService;

    /* -- 基本操作（上传、获取 url、删除） -- */

    /**
     * 详见：{@link FileStorageIService#uploadFile}
     * */
    @Override
    public String uploadFile(MultipartFile toUploadFile, FileStorageType storageType, UploadFileQO uploadFileQO) {
        return switch (storageType) {
            case LOCAL -> localStorageService.uploadFile(toUploadFile, uploadFileQO);
            case CLOUD -> cloudStorageService.uploadFile(toUploadFile, uploadFileQO);
        };
    }

    /**
     * 详见：{@link FileStorageIService#getFileUrl}
     * */
    @Override
    public String getFileUrl(String fileId) {
        if (localStorageService.mayBeStored(fileId))
            return localStorageService.getFileUrl(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.getFileUrl(fileId);

        return null;
    }

    /**
     * 详见：{@link FileStorageIService#deleteFile}
     * */
    @Override
    public boolean deleteFile(String fileId) {
        if (localStorageService.mayBeStored(fileId))
            return localStorageService.deleteFile(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.deleteFile(fileId);

        return true;
    }

    /* -- 读写纯文本文件 -- */

    /**
     * 详见：{@link FileStorageIService#writeTextToFile}
     */
    @Override
    public String writeTextToFile(FileStorageType storageType, String text, UploadFileQO uploadFileQO) {
        return switch (storageType) {
            case LOCAL -> localStorageService.writeTextToFile(text, uploadFileQO);
            case CLOUD -> cloudStorageService.writeTextToFile(text, uploadFileQO);
        };
    }

    /**
     * 详见：{@link FileStorageIService#readTextFromFile}
     */
    @Override
    public String readTextFromFile(String fileId) {
        if (localStorageService.mayBeStored(fileId))
            return localStorageService.readTextFromFile(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.readTextFromFile(fileId);

        return null;
    }
}
