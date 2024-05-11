package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnPublishReq {

    @Valid
    @JsonUnwrapped
    private AnnDetail announcement;

    @JsonProperty("draft_id")
    private Long draftId; /*  <- nullable，若不为 null 则发布公告后顺带删除该草稿 */
}
