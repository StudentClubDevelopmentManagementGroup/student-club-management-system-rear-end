package team.project.module.filestorage.internal.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.config.LocalFileStorageConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class LocalFileStorageDAO {

    /* 本地文件系统中，存储用户数据文件的根目录（在配置文件中以绝对路径形式给出）*/
    private final String rootFolder;

    LocalFileStorageDAO(LocalFileStorageConfig cfg) {
        this.rootFolder = cfg.rootFolder;
    }

    /**
     * 将上传的文件保存到指定路径下（如果文件已存在，则覆盖）
     * */
    public void save(MultipartFile uploadFile, String filePath) throws IOException {
        File fileToSave = new File(rootFolder, filePath);
        boolean ignored = fileToSave.getParentFile().mkdirs();

        uploadFile.transferTo(fileToSave);
    }

    /**
     * 将一段文本以 UTF8 编码保存到文本文件中（如果文件已存在，则覆盖）
     * */
    public void save(String text, String filePath) throws IOException {
        File fileToSave = new File(rootFolder, filePath);

        { boolean ignored = fileToSave.getParentFile().mkdirs(); }
        { boolean ignored = fileToSave.createNewFile(); }

        Files.writeString(fileToSave.toPath(), text, UTF_8);
    }

    /**
     * 将一段数据保存到文件中（如果文件已存在，则覆盖）
     * */
    public void save(byte[] data, String filePath) throws IOException {
        File fileToSave = new File(rootFolder, filePath);

        { boolean ignored = fileToSave.getParentFile().mkdirs(); }
        { boolean ignored = fileToSave.createNewFile(); }

        try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
            outputStream.write(data);
        }
    }

    /**
     * 判断文件是否存在
     * */
    public boolean isFileExist(String filePath) {
        return new File(rootFolder, filePath).exists();
    }

    /**
     * 删除文件
     * */
    public boolean delete(String filePath) {
        File file = new File(rootFolder,  filePath);
        return file.delete(); /* <- 只要真的删除成功，才返回是 true，其他情况都是 false */
    }
}
