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
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.export.service.FileStorageIService;
import team.project.module.filestorage.internal.service.impl.AliyunObjectStorageService;
import team.project.module.filestorage.export.service.impl.FileStorageIServiceImpl;
import team.project.module.filestorage.internal.service.impl.LocalFileSystemStorageService;

@Tag(name="文件存储")
@Controller
public class FileStorageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageIServiceImpl fileStorageService;

    @Autowired
    LocalFileSystemStorageService localStorageService;

    @Autowired
    AliyunObjectStorageService cloudStorageService;

    @Operation(summary="上传文件")
    @PostMapping("/upload_file")
    @ResponseBody
    Object uploadFile(
        @RequestParam("file")           MultipartFile   file,
        @RequestParam("storage_type")   String          storageType,
        @RequestParam("folder")         String          folder,
        @RequestParam("filename")       String          filename,
        @RequestParam(value="overwrite", required=false) Boolean overwrite
    ) {
        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }

        FileStorageIService.StorageType storageTypeEnum;
        try {
            storageTypeEnum = FileStorageIService.StorageType.valueOf(storageType);
        } catch (Exception e) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("无效的存储类型");
        }

        try {
            String fileId = fileStorageService.uploadFile(file, storageTypeEnum, folder, filename, overwrite != null && overwrite);
            return new Response<>(ServiceStatus.CREATED).statusText("上传成功").data(fileId);
        }
        catch (FileStorageException e) {
            if (FileStorageException.Status.FILE_EXIST.equals(e.getFileStorageExceptionStatus())) {
                return new Response<>(ServiceStatus.CONFLICT).statusText("文件已存在，且无法覆盖");
            } else {
                return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception e) {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary="获取访问已上传的文件的URL")
    @GetMapping("/get_uploaded_file_url")
    @ResponseBody
    Object getUploadedFileUrl(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        if (localStorageService.mayBeStored(fileId)) {
            String url = localStorageService.getUploadedFileUrl(fileId);
            return new Response<>(ServiceStatus.SUCCESS).data(url);
        }

        if (cloudStorageService.mayBeStored(fileId)) {
            String url = cloudStorageService.getUploadedFileUrl(fileId);
            return new Response<>(ServiceStatus.SUCCESS).data(url);
        }

        return new Response<>(ServiceStatus.BAD_REQUEST).statusText("无效的 file_id");
    }

    @Operation(summary="获取访问已上传的文件")
    @GetMapping("/get_uploaded_file")
    Object getUploadedFile(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        /* NOTE: 如果找不到文件，则重定向的地址是：“redirect:null”，响应 404 */
        return "redirect:" + fileStorageService.getUploadedFileUrl(fileId);
    }

    @Operation(summary="删除已上传的文件")
    @PostMapping("/delete_uploaded_file")
    @ResponseBody
    Object deleteUploadedFile(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        if (fileStorageService.deleteUploadedFile(fileId)) {
            return new Response<>(ServiceStatus.NO_CONTENT).statusText("删除成功");
        } else {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR).statusText("删除失败");
        }
    }
}
