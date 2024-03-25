package team.project.module.filestorage.internal.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.config.LocalFileSystemConfig;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;
import team.project.module.filestorage.internal.util.Util;

import java.io.IOException;

@Service
public class LocalFileSystemService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String uploadedFilesFolder;
    private final String uploadedFileIdPrefix;
    private final String baseUrl;

    @Autowired
    private LocalFileSystemDAO localFileSystemDAO;

    LocalFileSystemService(LocalFileSystemConfig cfg) {
        this.uploadedFilesFolder = cfg.uploadedFilesFolder;
        this.uploadedFileIdPrefix = cfg.uploadedFileIdPrefix;
        this.baseUrl = cfg.baseUrl;
    }

    public String upload(MultipartFile file) throws IOException {
        String newFilename = Util.generateRandomFileName(file.getOriginalFilename());

        String fileId = uploadedFileIdPrefix + "/" + newFilename;

        localFileSystemDAO.save(uploadedFilesFolder, newFilename, file);

        return fileId;
    }

    public String getUploadedFileUrl(String fileId) {
        if ( ! fileId.startsWith(uploadedFileIdPrefix)) {
            return null;
        }

        String filePath = uploadedFilesFolder + fileId.substring(uploadedFileIdPrefix.length());
        return baseUrl + filePath;
    }
}
