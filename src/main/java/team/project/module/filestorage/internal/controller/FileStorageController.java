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
import team.project.base.controller.exception.InvalidParamException;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.export.exception.FileStorageException;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.export.service.impl.FileStorageIServiceImpl;

@Tag(name="文件存储")
@Controller
public class FileStorageController {

    @Autowired
    FileStorageIServiceImpl fileStorageService;

    private FileStorageType toEnum(String storageType) {
        FileStorageType storageTypeEnum;
        try {
            storageTypeEnum = FileStorageType.valueOf(storageType);
        } catch (Exception e) {
            throw new InvalidParamException("无效的存储类型");
        }
        return storageTypeEnum;
    }

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
        FileStorageType storageTypeEnum = toEnum(storageType);

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
    Object uploadTextToFile(
        @RequestParam(value="content", defaultValue="")        String  text,
        @RequestParam("storage_type")                          String  storageType,
        @RequestParam("folder")                                String  folder,
        @RequestParam("filename")                              String  filename,
        @RequestParam(value="overwrite", defaultValue="false") Boolean overwrite
    ) {
        FileStorageType storageTypeEnum = toEnum(storageType);

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(folder);
        uploadFileQO.setTargetFilename(filename);
        uploadFileQO.setOverwrite(overwrite);

        try {
            String fileId = fileStorageService.uploadTextToFile(storageTypeEnum, text, uploadFileQO);

            return new Response<>(ServiceStatus.SUCCESS).data(fileId);
        }
        catch (FileStorageException e) {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("上传失败").data(e.getMessage());
        }
    }

    @Operation(summary="读取文本文件里的文本")
    @GetMapping("/get_text")
    @ResponseBody
    Object readTextFormFile(@NotBlank(message="未输入文件id") @RequestParam("file_id") String fileId) {
        String text;
        try {
            text = fileStorageService.readTextFromFile(fileId);
        }
        catch (FileStorageException e) {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("读取失败");
        }
        if (text == null) {
            return new Response<>(ServiceStatus.UNPROCESSABLE_ENTITY).statusText("读取失败");
        }
        else {
            return new Response<>(ServiceStatus.SUCCESS).data(text);
        }
    }
}
