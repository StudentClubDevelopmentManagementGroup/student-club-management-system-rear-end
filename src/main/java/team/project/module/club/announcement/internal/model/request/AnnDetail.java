package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import static team.project.module.club.announcement.internal.model.entity.AnnDO.SUMMARY_MAX_LENGTH;
import static team.project.module.club.announcement.internal.model.entity.AnnDO.TITLE_MAX_LENGTH;

/**
 * <p>  公告内容
 * <p>  不要单独使用本类来接收 controller 入参，
 *      而应与其他 Req 类组合使用
 * */
@Getter
@Setter
public class AnnDetail {

    @NotNull(message="未指定社团id")
    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @JsonIgnore
    private String authorId;

    @NotBlank(message="未指定标题")
    @Size(max=TITLE_MAX_LENGTH, message="标题过长")
    @JsonProperty("title")
    private String title;

    @NotBlank(message="未指定内容")
    @JsonProperty("content")
    private String content;

    @NotBlank(message="未指定摘要")
    @Size(max=SUMMARY_MAX_LENGTH, message="摘要过长")
    @JsonProperty("summary")
    private String summary;
}
