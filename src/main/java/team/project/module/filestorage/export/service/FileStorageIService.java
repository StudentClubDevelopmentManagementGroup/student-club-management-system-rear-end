package team.project.module.filestorage.export.service;

import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.exception.FileStorageException;

public interface FileStorageIService {

    /* 存储类型 */
    enum StorageType {
        LOCAL, /* 本地存储 */
        CLOUD, /* 云存储 */
    }

    /**
     * 上传文件
     * @param toUploadFile      要上传的文件
     * @param storageType       存储类型
     * @param targetFolder      目标目录（路径分隔符用'/'，路径以'/'开头，根目录用"/"表示）（如果传 null 或 ""，则使用根目录）
     * @param targetFilename    目标文件名（不包括扩展名）（如果传 null 或 ""，则使用 {@code toUploadFile} 的原文件名）
     * @param overwrite         如果文件已存在，是否覆盖
     * @return fileId
     * @throws FileStorageException
     *      <li>如果文件已存在，且 {@code overwrite} 为 false
     *      <li>或是上传途中遇到其他异常
     * */
    String uploadFile(MultipartFile toUploadFile, StorageType storageType, String targetFolder, String targetFilename, boolean overwrite);
/*  TODO: ljh_FIXME:FIXME: targetFolder禁止使用".."和"."越界访问其他文件夹的文件 */

    /**
     * <p>通过 fileId 获取访问该文件的 URL</p>
     * <p>但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <li>文件不存在（此时访问该 URL 可能会返回 404）</li>
     * <li>URL 设置了有效访问时长，访问时可能已经过期</li>
     * <li>...</li>
     * </p>
     * @return 如果 fileId 符合存储规则返回 URL，否则返回 null
     * */
    String getUploadedFileUrl(String fileId);

    /**
     * 删除 fileId 指向的文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 删除成功返回 true，否则返回 false
     * */
    boolean deleteUploadedFile(String fileId);
}
