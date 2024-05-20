package team.project.module.util.filestorage.internal.util;

public class Util {

    /**
     * <p>  按如下规则处理路径中的分割符：
     * <ol>
     * <li> 替换 '\' 为 '/'，统一使用 '/' 作为文件夹的分隔符
     * <li> 合并连续的斜杠为单个斜杠
     * <li> 移除路径末尾的斜杠（如果存在）
     * <li> <u>不移除</u>路径最打头的斜杠（如果存在）
     * <li> <u>不处理</u>路径中的 ".." 和 "."
     * </ol>
     *
     * @return 处理后的路径字符串
     * */
    public static String fixSeparator(String path) {
        String replaced = path.replace('\\', '/').replaceAll("/+", "/");
        return replaced.endsWith("/") ? replaced.substring(0, replaced.length() - 1) : replaced;
    }

    /**
     * <p> 判断文件路径的书写是否<b>不符合</b>规则
     * <p> 判断路径是否有效有诸多步骤，而这里只判断一部分
     * <p> 路径的书写规则见本模块的 package-info.java
     * */
    public static boolean isNotValidFilePath(String filePath) {
        return hasInvalidChar(filePath)
          ||   hasRelativePathPart(filePath)
          ||   filePath.endsWith(".")
          || ! filePath.equals(fixSeparator(filePath))
        ;
    }

    /**
     * 判断文件路径中是否含有非法字符
     * */
    public static boolean hasInvalidChar(String filePath) {
        return -1 != filePath.indexOf(':')
            || -1 != filePath.indexOf('*')
            || -1 != filePath.indexOf('?')
            || -1 != filePath.indexOf('\"')
            || -1 != filePath.indexOf('\\')
            || -1 != filePath.indexOf('<')
            || -1 != filePath.indexOf('>')
            || -1 != filePath.indexOf('|');
    }

    /**
     * 判断文件路径中是否含 "/.." 或 "/."表示相对路径的部分
     * */
    public static boolean hasRelativePathPart(String filePath) {
        return filePath.contains("/.");
    }
}
