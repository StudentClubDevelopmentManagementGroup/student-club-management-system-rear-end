package team.project.module.filestorage.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.export.service.FileStorageIService;


@Tag(name="文件存储")
@RestController
public class FileStorageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageIService service;

    @Operation(summary="上传文件至服务器的本地文件系统", description="如果上传成功，data返回文件id")
    @PostMapping("/upload_file_to_local_file_system")
    Object uploadFileToLocalFileSystem(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }

        try {
            String newFileName = service.uploadFileToLocalFileSystem(file);
            return new Response<>(ServiceStatus.CREATED)
                .statusText("上传成功").data(newFileName);

        } catch (Exception e) {
            logger.error("上传文件至服务器的本地文件系统失败", e);
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR).data("上传失败");
        }
    }

    @Operation(summary="上传文件至云存储空间", description="如果上传成功，data返回文件id")
    @PostMapping("/upload_file_to_cloud_storage")
    Object uploadFileToCloudStorage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }
        try {
            String newFileName = service.uploadFileToCloudStorage(file);
            return new Response<>(ServiceStatus.CREATED).statusText("上传成功").data(newFileName);

        } catch (Exception e) {
            logger.error("上传文件至服务器的本地文件系统失败", e);
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR).data("上传失败");
        }
    }

    @Operation(summary="获取访问已上传的文件的URL")
    @GetMapping("/get_uploaded_file_url")
    Object getUploadedFileUrl(
        @NotBlank(message="未输入文件id")
        @RequestParam("file_id") String fileId
    ) {
        String url = service.getUploadedFileUrl(fileId);
        if (url == null) {
            return new Response<>(ServiceStatus.NOT_FOUND);
        } else {
            return new Response<>(ServiceStatus.SUCCESS).data(url);
        }
    }
}
