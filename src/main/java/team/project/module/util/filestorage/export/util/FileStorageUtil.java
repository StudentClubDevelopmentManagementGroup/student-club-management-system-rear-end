package team.project.module.util.filestorage.export.util;

import java.util.UUID;

public class FileStorageUtil {

    /**
     * 生成随机的文件名
     * @param extension 文件后缀
     * */
    public static String randomFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
