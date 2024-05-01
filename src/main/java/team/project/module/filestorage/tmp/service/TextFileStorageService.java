package team.project.module.filestorage.tmp.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.internal.config.LocalFileStorageConfig;
import team.project.module.filestorage.internal.service.impl.LocalFileStorageService;
import team.project.module.filestorage.internal.util.Util;
import team.project.module.filestorage.tmp.request.UploadTextReq;

import static team.project.module.filestorage.export.exception.FileStorageException.Status.*;

@Service
public class TextFileStorageService extends LocalFileStorageService{

    TextFileStorageService(LocalFileStorageConfig cfg) {
        super(cfg);
    }

    public String uploadText(UploadTextReq req) {
        return uploadText(req.getContent(), uploadedFilesFolder, Util.generateRandomFileName("x.txt"), true);
    }

    private String uploadText(String content, String targetFolder, String targetFilename, boolean overwrite) {

        String fileId = generateFileId(targetFolder, targetFilename);
        if ( ! Util.isValidFileId(fileId)) {
            throw new FileStorageException(INVALID_FILE_PATH, "目标目录路径或目标文件名不合约束");
        }

        String filePath = parseFileIdToFilePath(fileId);

        if ( ! overwrite && localFileStorageDAO.isFileExist(filePath)) {
            throw new FileStorageException(FILE_EXIST, "文件已存在，且无法覆盖");
        }

        try {
            localFileStorageDAO.save(content, filePath);
            return fileId;
        }
        catch (Exception e) {
            log.error("上传文件到本地文件系统时出现异常", e);
            throw new FileStorageException(UNSOLVABLE, "上传文件失败");
        }
    }
}
