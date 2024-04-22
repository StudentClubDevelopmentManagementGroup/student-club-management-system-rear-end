package team.project.module.filestorage.export.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageIService {

    /**
     * 上传文件到本地文件系统
     * @return 如果上传成功，返回 fileId，如果上传途中出现异常则返回 null
     * */
    String uploadFileToLocalFileSystem(MultipartFile file);

    /**
     * 上传文件到云存储空间
     * @return 如果上传成功，返回 fileId，如果上传途中出现异常则返回 null
     * */
    String uploadFileToCloudStorage(MultipartFile file);

    /**
     * <p>通过 fileId 获取访问该文件的 URL</p>
     * <p>但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <li>文件不存在，此时访问该 URL 可能会返回 404 </li>
     * <li>URL 设置了有效访问时长，访问时可能已经过期</li>
     * <li>...</li>
     * </p>
     *
     * @return 访问该文件的 URL，或 null
     * <li>如果 fileId 不符合格式，则返回 null</li>
     * <li>如果 fileId 符合格式，则尝试获取 URL</li>
     * <li>如果获取成功，则返回 URL</li>
     * <li>如果获取失败（获取时出现异常）否则返回 null</li>
     * */
    String getUploadedFileUrl(String fileId);

    /**
     * 删除文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 执行时没有发生异常则返回 true，否则返回 false
     * */
    boolean deleteUploadedFile(String fileId);
}
