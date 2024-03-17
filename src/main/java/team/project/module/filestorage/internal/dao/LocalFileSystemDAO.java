package team.project.module.filestorage.internal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.config.FileStorageConfig;

import java.io.File;
import java.io.IOException;


@Component
public class LocalFileSystemDAO {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String rootFolder;

    LocalFileSystemDAO(FileStorageConfig cfg) {
        this.rootFolder = cfg.rootFolder;
    }

    public void write(String folder, String fileName, MultipartFile file) throws IOException {
        File targetFolder = new File(rootFolder + "/" + folder).getCanonicalFile();
        boolean ignored = targetFolder.mkdirs();
        File targetFile = new File(targetFolder + "/" + fileName).getCanonicalFile();
        file.transferTo(targetFile);
    }
}
