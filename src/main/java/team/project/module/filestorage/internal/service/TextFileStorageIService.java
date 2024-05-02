package team.project.module.filestorage.internal.service;

import team.project.module.filestorage.export.model.query.UploadFileQO;

public interface TextFileStorageIService {

    String saveTextToFile(String text, UploadFileQO uploadFileQO);
}
