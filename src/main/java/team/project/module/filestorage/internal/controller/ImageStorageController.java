package team.project.module.filestorage.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.internal.service.ImageStorageService;


@Tag(name="ImageStorageController")
@RestController
public class ImageStorageController {

    @Autowired
    ImageStorageService service;

    @Operation(summary="上传图片至 /resources")
    @PostMapping("/upload-image-to-resources")
    Object uploadImageToResources(@RequestParam("image-file") MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).statusText("上传的图片文件为空");
        }

        String newFileName = service.uploadImageToResource(imageFile);

        return new Response<>(ServiceStatus.CREATED).data(newFileName);
    }

    @Operation(summary="获取定位到 “上传至 /resources 的图片” 的 url")
    @GetMapping("/get-url-to-uploaded-image-in-resources")
    Object getImageFormResources(
        @NotNull(message="未输入图片名")
        @NotBlank(message="未输入图片名")
        @Size(min=10, max=255, message="图片名长度必须在 1 到 255 个字符之间")
        @RequestParam("image-file-name") String imageFileName
    ) {
        String redirectUrl = service.getRedirectUrlToImageFileInResources(imageFileName);
        if (redirectUrl == null) {
            return new Response<>(ServiceStatus.NOT_FOUND);
        } else {
            return new Response<>(ServiceStatus.SUCCESS).data(redirectUrl);
        }
    }
}
