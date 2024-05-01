package team.project.module.filestorage.tmp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.filestorage.tmp.request.UploadTextReq;
import team.project.module.filestorage.tmp.service.TextFileStorageService;

@Tag(name="文件存储，测试")
@RestController
public class TextFileStorageController {

    @Autowired
    TextFileStorageService textFileStorageService;

    @Operation(summary="上传一段文本，并将其保存为文本文件")
    @PostMapping("/upload_text")
    @ResponseBody
    Object uploadFile(@Valid UploadTextReq req) {
        System.out.println("title: " + req.getTitle());
        System.out.println("content: " + req.getContent());

        return new Response<>(ServiceStatus.SUCCESS)
            .data(textFileStorageService.uploadText(req));
    }
}
