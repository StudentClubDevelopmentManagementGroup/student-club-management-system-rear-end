package team.project.module.club.announcement.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;

@Tag(name="公告")
@RestController
@RequestMapping("/club/announcement/draft")
public class DraftController {

    @Operation(summary="保存草稿")
    @PostMapping("/save")
    Object saveDraft() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary="获取草稿内容")
    @GetMapping("/read")
    Object readDraft() {
        return new Response<>(ServiceStatus.NOT_IMPLEMENTED);
    }
}
