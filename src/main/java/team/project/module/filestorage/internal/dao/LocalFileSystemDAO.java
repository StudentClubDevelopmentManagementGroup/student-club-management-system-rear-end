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

    /* 本地文件系统中，存储用户数据文件的根目录（在配置文件中以绝对路径形式给出）*/
    private final String rootFolder;

    LocalFileSystemDAO(LocalFileSystemConfig cfg) {
        this.rootFolder = cfg.rootFolder;
    }

    public void save(String filePath, MultipartFile file) throws IOException {
        File fileToSave = new File(rootFolder, filePath);
        boolean ignored = fileToSave.getParentFile().mkdirs();
        file.transferTo(fileToSave);
    }

    public boolean delete(String filePath) {
        File file = new File(rootFolder,  filePath);
        return file.delete(); /* <- 只要真的删除成功，才返回是 true，其他情况都是 false */
    }
}
