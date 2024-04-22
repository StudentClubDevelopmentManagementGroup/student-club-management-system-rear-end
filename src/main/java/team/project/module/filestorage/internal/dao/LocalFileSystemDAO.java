package team.project.module.filestorage.internal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.config.LocalFileSystemConfig;

import java.io.File;
import java.io.IOException;

@Component
public class LocalFileSystemDAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String rootFolder;

    LocalFileSystemDAO(LocalFileSystemConfig cfg) {
        this.rootFolder = cfg.rootFolder;
    }

    public void save(String targetFolder, String fileName, MultipartFile file) throws IOException {
        File folder = new File(rootFolder + "/" + targetFolder);
        boolean ignored = folder.mkdirs();
        file.transferTo(new File(folder + "/" + fileName));
    }

    public boolean delete(String uploadedFilesFolder, String fileName) {
        File file = new File(rootFolder + "/" + uploadedFilesFolder + "/" + fileName);
        return file.delete(); /* <- 只要真的删除成功，才返回是 true，其他情况都是 false */
    }
}
