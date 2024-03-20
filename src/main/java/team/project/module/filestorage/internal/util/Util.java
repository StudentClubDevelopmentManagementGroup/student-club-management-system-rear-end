package team.project.module.filestorage.internal.util;

import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

public class Util {
    public static String fixPath(String path) {
        /* 将'\'替换为'/'，然后将多个连续'/' 替换为一个'/' */
        String replaced = path.replace('\\', '/').replaceAll("/+", "/");
        /* 最后确保不以'/'结尾 */
        return replaced.endsWith("/") ? replaced.substring(0, replaced.length() - 1) : replaced;
    }

    public static String generateRandomFileName(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        return UUID.randomUUID().toString().replace("-", "") + ("".equals(extension) ? "" : "." + extension);
    }
}
