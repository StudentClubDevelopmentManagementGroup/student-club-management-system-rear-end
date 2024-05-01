package team.project.module.filestorage.internal.util;

import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

public class Util {

    /**
     * <p>将文件路径字符串进行格式化：
     *  <li> 统一使用'/'作为文件夹的分隔符</li>
     *  <li> 合并连续的斜杠为单个斜杠</li>
     *  <li> 并移除路径末尾的斜杠（如果存在）</li>
     *  <li> 不移除路径最打头的斜杠（如果存在）</li>
     *  <li> 不处理路径中的..和.</li>
     * </p>
     * @return 格式化后的路径字符串
     * */
    public static String fixPath(String path) {
        String replaced = path.replace('\\', '/').replaceAll("/+", "/");
        return replaced.endsWith("/") ? replaced.substring(0, replaced.length() - 1) : replaced;
    }

    /* fileId 不允许出现的非法字符集 */
    private static final String[] ILLEGAL_CHARS = { ":", "*", "?", "\"", "<", ">", "|" };

    /**
     * 判断 fileId 是否符合约束
     * 没有出现非法字符“:*?"'<>|”，路径中没有出现“/..”和“/.”
     * */
    public static boolean isValidFileId(String fileId) {
        if (fileId.endsWith(".") || fileId.startsWith(".") || fileId.contains("/.")) {
            return false;
        }
        for (String illegal : ILLEGAL_CHARS) {
            if (fileId.contains(illegal)) {
                return false;
            }
        }
        return true;
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
