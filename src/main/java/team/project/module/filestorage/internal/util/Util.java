package team.project.module.filestorage.internal.util;

import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

public class Util {

    /**
     * 将文件路径字符串进行格式化：
     *  <li> 统一使用'/'作为文件夹的分隔符
     *  <li> 合并连续的斜杠为单个斜杠
     *  <li> 并移除路径末尾的斜杠（如果存在）
     * @return 格式化后的路径字符串
     * */
    public static String fixPath(String path) {
        String replaced = path.replace('\\', '/').replaceAll("/+", "/");
        return replaced.endsWith("/") ? replaced.substring(0, replaced.length() - 1) : replaced;
    }

    /**
     * 生成随机的文件名（保留文件扩展名）
     * @return 随机文件名
     * */
    public static String generateRandomFileName(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        return UUID.randomUUID().toString().replace("-", "") + ("".equals(extension) ? "" : "." + extension);
    }
}
