package team.project.module.filestorage.internal.service;

import team.project.module.filestorage.export.model.query.UploadFileQO;

public interface TextFileStorageIService {

    /**
     * 将一段文本保存到 .txt 文件中
     * @param text         要保存的文本
     * @param uploadFileQO 详见 {@link UploadFileQO}（其中，必须要指定文件名）
     * */
    String writeTextToFile(String text, UploadFileQO uploadFileQO);

    /**
     * 读取纯文本文件里的内容
     * */
    String readTextFromFile(String fileId);
}
