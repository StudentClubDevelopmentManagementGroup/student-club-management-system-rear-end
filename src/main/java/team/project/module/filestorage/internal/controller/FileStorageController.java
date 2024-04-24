package team.project.module.filestorage.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.export.service.FileStorageIService;

@Tag(name="文件存储")
@Controller
public class FileStorageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageIService service;

    @Operation(summary="上传文件至服务器的本地文件系统", description="如果上传成功，data返回文件id")
    @PostMapping("/upload_file_to_local_file_system")
    @ResponseBody
    Object uploadFileToLocalFileSystem(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }

        String fileId = service.uploadFileToLocalFileSystem(file, "/");
        if (fileId != null) {
            return new Response<>(ServiceStatus.CREATED).statusText("上传成功").data(fileId);
        }
        else {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR).statusText("上传失败");
        }
    }

    @Operation(summary="上传文件至云存储空间", description="如果上传成功，data返回文件id")
    @PostMapping("/upload_file_to_cloud_storage")
    @ResponseBody
    Object uploadFileToCloudStorage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }

        String fileId = service.uploadFileToCloudStorage(file, "/");
        if (fileId != null) {
            return new Response<>(ServiceStatus.CREATED).statusText("上传成功").data(fileId);
        }
        else {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR).statusText("上传失败");
        }
    }

    @Operation(summary="获取访问已上传的文件的URL")
    @GetMapping("/get_uploaded_file_url")
    @ResponseBody
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

    @Operation(summary="获取访问已上传的文件")
    @GetMapping("/get_uploaded_file")
    Object getUploadedFile(
        @NotBlank(message="未输入文件id")
        @RequestParam("file_id") String fileId
    ) {
        /* NOTE: 如果找不到文件，则重定向的地址是：“redirect:null”，响应 404 */
        return "redirect:" + service.getUploadedFileUrl(fileId);
    }

    @Operation(summary="删除已上传的文件")
    @PostMapping("/delete_uploaded_file")
    @ResponseBody
    Object deleteUploadedFile(
        @NotBlank(message="未输入文件id")
        @RequestParam("file_id") String fileId
    ) {
        if (service.deleteUploadedFile(fileId)) {
            return new Response<>(ServiceStatus.NO_CONTENT).statusText("删除成功");
        }
        else {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR).statusText("删除失败");
        }
    }
}
