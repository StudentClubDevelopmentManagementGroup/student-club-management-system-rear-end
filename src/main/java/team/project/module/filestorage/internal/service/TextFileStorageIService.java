package team.project.module.filestorage.internal.service;

import team.project.module.filestorage.export.model.query.UploadFileQO;

public interface TextFileStorageIService {

    /**
     * 上传文件到指定目录
     * @param content      要上传的文件
     * @param uploadFileQO 详见：{@link UploadFileQO}
     * @return fileId
     * */
    String uploadText(String content, UploadFileQO uploadFileQO);
}
