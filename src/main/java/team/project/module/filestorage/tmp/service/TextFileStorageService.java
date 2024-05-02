package team.project.module.filestorage.tmp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.internal.service.impl.LocalFileStorageService;
import team.project.module.filestorage.tmp.request.UploadTextReq;

@Service
public class TextFileStorageService {

    @Autowired
    LocalFileStorageService localFileStorageService;

    public String uploadText(UploadTextReq req) {

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder("/text");
        uploadFileQO.setTargetFilename(req.getTitle());
        uploadFileQO.setOverwrite(true);

        return localFileStorageService.saveTextToFile(req.getContent(), uploadFileQO);
    }

}
