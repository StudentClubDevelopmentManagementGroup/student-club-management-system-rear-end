package team.project.module.filestorage.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.internal.service.FileStorageService;


@Tag(name="【测试】文件存储")
@RestController
public class FileStorageController {

    @Autowired
    FileStorageService service;

    @Operation(summary="上传文件至服务器的本地文件系统")
    @PostMapping("/upload-file-to-local-file-system")
    Object uploadFileToLocalFileSystem(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }

        String newFileName = service.uploadFileToLocalFileSystem(file);

        return new Response<>(ServiceStatus.CREATED).statusText("上传成功").data("文件id：" + newFileName);
    }


    @Operation(summary="从服务器的本地文件系统获取文件")
    @GetMapping("/get-uploaded-file-from-local-file-system")
    Object getUploadedFileFromLocalFileSystem(
        @NotBlank(message="未输入文件id")
        @RequestParam("file-id") String fileId
    ) {
        return new RedirectView("/upload/" + fileId);
    }
}
