package team.project.module.filestorage.export.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.LocalFileSystemService;

@Service("file-local-storage")
public class FileLocalStorageIServiceImpl implements FileStorageIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LocalFileSystemService localFileSystemService;

    @Override
    public String uploadFile(MultipartFile file, String targetFolder, String filename, boolean overwrite) {
        return localFileSystemService.upload(file, targetFolder, filename, overwrite);
    }

    @Override
    public boolean isFileExist(String fileId) {
        return localFileSystemService.isFileExist(fileId);
    }

    @Override
    public String getUploadedFileUrl(String fileId) {
        return localFileSystemService.getUploadedFileUrl(fileId);
    }

    @Override
    public boolean deleteUploadedFile(String fileId) {
        return localFileSystemService.deleteUploadedFile(fileId);
    }
}
