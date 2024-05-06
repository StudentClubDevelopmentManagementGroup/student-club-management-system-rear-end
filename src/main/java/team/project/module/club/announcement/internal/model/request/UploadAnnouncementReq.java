package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Getter
@Setter
public class UploadAnnouncementReq {
    @NotNull(message="未指定社团id")
    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @NotBlank(message="未指定公告标题")
    @JsonProperty("title")
    private String title;

    @NotBlank(message="未指定公告内容")
    @JsonProperty("content")
    private String content;
}
