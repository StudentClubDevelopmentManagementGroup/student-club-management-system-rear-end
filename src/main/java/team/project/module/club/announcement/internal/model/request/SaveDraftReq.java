package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import static team.project.module.club.announcement.internal.model.entity.AnnouncementDO.TitleMaxLength;

@Getter
@Setter
public class SaveDraftReq {

    @JsonProperty("draft_id")
    private Long draftId; /* <- nullable，若为 null 则上传新草稿，否则是更新草稿 */

    @NotNull(message="未指定社团id")
    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @NotBlank(message="未指定标题")
    @Size(max=TitleMaxLength, message="标题过长")
    @JsonProperty("title")
    private String title;

    @NotBlank(message="未指定内容")
    @JsonProperty("content")
    private String content;
}
