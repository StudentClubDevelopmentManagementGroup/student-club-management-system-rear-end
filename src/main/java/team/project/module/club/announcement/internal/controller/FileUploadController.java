package team.project.module.club.announcement.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.enums.FileStorageType;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;
import team.project.module.util.filestorage.export.util.FileStorageUtil;

@Tag(name="公告（文件存取）")
@Controller("club-announcement-FileUploadController")
@RequestMapping("/club/announcement/file")
public class FileUploadController {

    @Autowired
    FileStorageServiceI fileStorageService;

    private static final String UPLOAD_FILE_FOLDER = "/club/announcement/file";

    private static final FileStorageType STORAGE_TYPE = FileStorageType.CLOUD;

    @Operation(summary="上传文件")
    @PostMapping("/upload")
    @ResponseBody
    @SaCheckRole(AuthRole.CLUB_MANAGER)
    Object upload(@RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }

        try {
            UploadFileQO uploadFileQO = new UploadFileQO();
            uploadFileQO.setTargetFolder(UPLOAD_FILE_FOLDER);
            uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(file));
            uploadFileQO.setOverwrite(false);

            String fileId = fileStorageService.uploadFile(file, STORAGE_TYPE, uploadFileQO);

            return new Response<>(ServiceStatus.CREATED).statusText("上传成功").data(fileId);
        }
        catch (FileStorageException e) {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("上传失败").data(e.getMessage());
        }
    }

    @Operation(summary="获取已上传的文件")
    @GetMapping("/get")
    @ResponseBody
    Object get(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        String url = fileStorageService.getFileUrl(fileId);
        return url == null ? "" : ("redirect:" + url);
    }
}
