package team.project.module.util.filestorage.export.util;

import java.util.UUID;

public class FileStorageUtil {

    /**
     * 生成指定后缀的随机的文件名
     * @param extension 文件后缀（例子：".txt"、".html"、".png"）
     * */
    public static String randomFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
