package team.project.module.filestorage.internal.service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;

public abstract class FileStorageAService {

    /* fileId 不允许出现的非法字符集 */
    private final static char[] ILLEGAL_CHARS = new char[]{':', '*', '?', '"', '<', '>', '|'};

    /**
     * 判断 fileId 是否符合约束
     * 没有出现非法字符“:*?"'<>|”，路径中没有出现“/..”和“/.”
     * */
    protected boolean isValidFileId(String fileId) {
        if (fileId.endsWith(".") || fileId.startsWith(".") || fileId.contains("/.")) {
            return false;
        }
        for (char illegal : ILLEGAL_CHARS) {
            if (fileId.indexOf(illegal) != -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 上传文件到指定目录
     * @param toUploadFile   要上传的文件
     * @param targetFolder   目标目录（路径分隔符用'/'，如果传 null 或 ""，则使用根目录）
     * @param targetFilename 目标文件名（不包括扩展名，如果传 null 或 ""，则使用 {@code toUploadFile} 的原文件名）
     * @param overwrite      如果文件已存在，是否覆盖
     * @return fileId
     * @throws FileStorageException
     *      <li>目标目录路径或目标文件名不合约束
     *      <li>如果文件已存在，且 {@code overwrite} 为 false
     *      <li>或是上传途中遇到其他异常
     * */
    abstract public String uploadFile(MultipartFile toUploadFile, String targetFolder, String targetFilename, boolean overwrite);

    /**
     * <p>  通过 fileId 判断文件是否可能在此存储空间中</p>
     * <p>  如果文件在此存储空间中，则 fileId 一定符合规则
     * <br> 操作文件前，先判断 fileId 是否符合这个规则
     * <br> 若不符合，则认为文件不存在，不必再进行后续操作</p>
     * */
    abstract public boolean mayBeStored(String fileId);

    /**
     * <p>通过 fileId 获取访问该文件的 URL</p>
     * <p>但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <li>文件不存在（此时访问该 URL 可能会返回 404）</li>
     * <li>URL 设置了有效访问时长，访问时可能已经过期</li>
     * <li>...</li>
     * </p>
     * @return 如果 fileId 符合约束，且符合存储规则，返回 URL，否则返回 null
     * */
    abstract public String getUploadedFileUrl(String fileId);

    /**
     * 删除 fileId 指向的文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 删除成功返回 true，否则返回 false
     * */
    abstract public boolean deleteUploadedFile(String fileId);
}
