package team.project.module.filestorage.internal.util;

import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

public class Util {

    /**
     * <p>  按如下规则处理路径中的文件分割符：
     * <ol>
     * <li> 统一使用 '/' 作为文件夹的分隔符
     * <li> 合并连续的斜杠为单个斜杠
     * <li> 并移除路径末尾的斜杠（如果存在）
     * <li> 不移除路径最打头的斜杠（如果存在）
     * </ol>
     *
     * <p>  不处理路径中的 ".." 和 "."
     * <p>  更多路径处理函数，见工具类： {@link FilenameUtils}
     *
     * @return 处理后的路径字符串
     * */
    public static String fixSeparator(String path) {
        String replaced = path.replace('\\', '/').replaceAll("/+", "/");
        return replaced.endsWith("/") ? replaced.substring(0, replaced.length() - 1) : replaced;
    }

    /** fileId 不允许出现的非法字符集 */
    private static final String[] ILLEGAL_CHARS = { ":", "*", "?", "\"", "<", ">", "|" };

    /**
     * <p>  判断 fileId 是否符合约束：
     * <ol>
     * <li> 不以 "." 开头，不以 "." 结尾
     * <li> 不含 "/.." 或 "/."
     * <li> 不含非法字符： * : ? " ' < > |
     * </ol>
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
     * 重命名文件，保留原扩展名
     * */
    public static String rename(String originalFilename, String newFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        return newFilename + ("".equals(extension) ? "" : "." + extension);
    }

    /**
     * 生成随机的文件名（保留文件扩展名）
     * */
    public static String generateRandomFileName(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        return UUID.randomUUID().toString().replace("-", "") + ("".equals(extension) ? "" : "." + extension);
    }
}
