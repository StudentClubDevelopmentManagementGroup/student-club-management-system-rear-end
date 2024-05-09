package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishAnnReq {

    @Valid
    @JsonUnwrapped
    private AnnDetail announcement;

    @JsonProperty("delete_draft")
    private Long deleteDraft; /*  <- nullable，若不为 null 则发布时顺带删除草稿 */
}
