package team.project.module.filestorage.export.service;

import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;

public interface FileStorageIService {

    /**
     * 上传文件
     * @param toUploadFile 要上传的文件
     * @param storageType  存储类型
     * @param uploadFileQO 详见：{@link UploadFileQO}
     * @return fileId
     * */
    String uploadFile(MultipartFile toUploadFile, FileStorageType storageType, UploadFileQO uploadFileQO);

    /**
     * <p>通过 fileId 获取访问该文件的 URL</p>
     * <p>但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <li>文件不存在（此时访问该 URL 可能会返回 404）</li>
     * <li>URL 设置了有效访问时长，访问时可能已经过期</li>
     * <li>...</li>
     * </p>
     * @return 如果 fileId 符合约束，且符合存储规则，返回 URL，否则返回 null
     * */
    String getUploadedFileUrl(String fileId);

    /**
     * 删除 fileId 指向的文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 删除成功返回 true，否则返回 false
     * */
    boolean deleteUploadedFile(String fileId);
}
