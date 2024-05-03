package team.project.module.filestorage.internal.service;

import team.project.module.filestorage.export.model.query.UploadFileQO;

public interface TextFileStorageIService {

    /**
     * 上传一段文本，将其保存到纯文本文件中
     * @param text         要保存的文本
     * @param uploadFileQO 详见 {@link UploadFileQO}（其中，必须要指定文件名）
     * @return fileId
     * */
    String uploadTextToFile(String text, UploadFileQO uploadFileQO);

    /**
     * 读取纯文本文件里的内容
     * @return 如果读取成功，则返回文本文件里的所有内容
     *    <br> 如果文件不在此存储空间中，或是读取中途出现异常，则返回 null
     * */
    String readTextFromFile(String fileId);
}
