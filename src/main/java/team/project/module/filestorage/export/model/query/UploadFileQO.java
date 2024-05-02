package team.project.module.filestorage.export.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileQO {

    /**
     * <p>  指定存储目录
     * <p>  根目录用 "/"，传 null 或 "" 也默认使用根目录
     *
     * <ol> 格式要求：
     * <li> 路径分隔符用 '/'
     * <li> 路径中不能出现 "/.." 和 "/."
     * <li> 路径中不能含非法字符： * : ? " ' < > |
     * */
    String targetFolder = "/";

    /**
     * 指定文件名（传 null 或 "" 则不指定）
     * */
    String targetFilename = null;

    /**
     * 如果文件已存在，是否覆盖
     * */
    boolean overwrite = false;

    /**
     * 判断是否指定存储目录
     * */
    public boolean isTargetRootFolder() {
        return targetFolder == null || targetFolder.isBlank();
    }

    public boolean isUsingOriginalFilename() {
        return targetFilename == null || targetFilename.isBlank();
    }
}
