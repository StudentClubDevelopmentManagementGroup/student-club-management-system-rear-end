package team.project.module.filestorage.export.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileQO {

    /**
     * <p>  指定存储目录
     * <ol> 格式要求：
     * <li> 路径分隔符用 '/'
     * <li> 路径中不能出现 "/.." 和 "/."
     * <li> 路径中不能含非法字符： * : ? " ' < > |
     * </ol>
     * <p>  根目录用 "/"，传 null 或 "" 也默认使用根目录
     * */
    String targetFolder = "/";

    /**
     * <p> 指定文件名（包括扩展名）
     * <p> 传 null 或 "" 则不指定
     * */
    String targetFilename = null;

    /**
     * <p>  如果文件已存在，是否覆盖
     * <p>  注意，该值传 ture 仍可能覆盖失败，例如：
     * <br> 当文件系统中存在文件：/a/b/c.txt 时
     * <br> 存在文件夹：/a/b
     * <br> 此时，上传文件到目录 /a，且文件名指定是 b（无扩展名）
     * <br> 即使 overwrite = true，也无法覆盖
     * <br> 因为文件系统中，文件夹和文件不能同名，这里命名 'b' 冲突
     * */
    boolean overwrite = false;
}
