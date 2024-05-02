package team.project.module.filestorage.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.export.service.impl.FileStorageIServiceImpl;
import team.project.module.filestorage.internal.service.impl.LocalFileStorageService;

@Tag(name="文件存储")
@Controller
public class FileStorageController {

    @Autowired
    FileStorageIServiceImpl fileStorageService;

    @Autowired
    LocalFileStorageService localFileStorageService;

    @Operation(summary="上传文件")
    @PostMapping("/upload_file")
    @ResponseBody
    Object uploadFile(
        @RequestParam("file")         MultipartFile file,
        @RequestParam("storage_type") String        storageType,
        @RequestParam("folder")       String        folder,
        @RequestParam("filename")     String        filename,
        @RequestParam(value="overwrite", defaultValue="false") Boolean overwrite
    ) {
        if (file == null || file.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("上传的文件为空");
        }

        FileStorageType storageTypeEnum;
        try {
            storageTypeEnum = FileStorageType.valueOf(storageType);
        } catch (Exception e) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data("无效的存储类型");
        }

        try {
            UploadFileQO uploadFileQO = new UploadFileQO();
            uploadFileQO.setTargetFolder(folder);
            uploadFileQO.setTargetFilename(filename);
            uploadFileQO.setOverwrite(overwrite);

            String fileId = fileStorageService.uploadFile(file, storageTypeEnum, uploadFileQO);

            return new Response<>(ServiceStatus.CREATED).statusText("上传成功").data(fileId);
        }
        catch (FileStorageException e) {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("上传失败").data(e.getMessage());
        }
    }

    @Operation(summary="获取访问已上传的文件的URL")
    @GetMapping("/get_file_url")
    @ResponseBody
    Object getUploadedFileUrl(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        String url = fileStorageService.getFileUrl(fileId);
        if (url != null) {
            return new Response<>(ServiceStatus.SUCCESS).data(url);
        }
        else {
            return new Response<>(ServiceStatus.NOT_FOUND).statusText("无效的 file_id");
        }
    }

    @Operation(summary="获取访问已上传的文件")
    @GetMapping("/get_file")
    Object getUploadedFile(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        /* NOTE: 如果找不到文件，则重定向的地址是：“redirect:”，响应 404 */
        String url = fileStorageService.getFileUrl(fileId);
        return url == null ? "" : ("redirect:" + url);
    }

    @Operation(summary="删除已上传的文件")
    @PostMapping("/delete_file")
    @ResponseBody
    Object deleteUploadedFile(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        if (fileStorageService.deleteFile(fileId)) {
            return new Response<>(ServiceStatus.NO_CONTENT).statusText("删除成功");
        } else {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("删除失败");
        }
    }

    /* --------- */

    @Operation(summary="上传一段文本，将其保存为文本文件")
    @PostMapping("/upload_text")
    @ResponseBody
    Object uploadFile(
        @RequestParam("content")   String  content,
        @RequestParam("folder")    String  folder,
        @RequestParam("filename")  String  filename,
        @RequestParam(value="overwrite", defaultValue="false") Boolean overwrite
    ) {
        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(folder);
        uploadFileQO.setTargetFilename(filename);
        uploadFileQO.setOverwrite(overwrite);


        try {
            String fileId = localFileStorageService.writeTextToFile(content, uploadFileQO);
            return new Response<>(ServiceStatus.SUCCESS).data(fileId);
        }
        catch (FileStorageException e) {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("上传失败").data(e.getMessage());
        }
    }

    @Operation(summary="读取文本文件里的文本")
    @GetMapping("/get_text")
    @ResponseBody
    Object readFile(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        try {
            String text = localFileStorageService.readTextFromFile(fileId);
            return new Response<>(ServiceStatus.SUCCESS).data(text);
        }
        catch (FileStorageException e) {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("读取失败");
        }
    }
}
