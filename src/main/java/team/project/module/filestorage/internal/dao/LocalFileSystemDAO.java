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

    public void write(String targetFolder, String fileName, MultipartFile file) throws IOException {
        File folder = new File(rootFolder + "/" + targetFolder).getCanonicalFile();
        boolean ignored = folder.mkdirs();
        File targetFile = new File(folder + "/" + fileName).getCanonicalFile();
        file.transferTo(targetFile);
    }
}
