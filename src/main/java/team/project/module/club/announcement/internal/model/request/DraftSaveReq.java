package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DraftSaveReq {

    @Valid
    @JsonUnwrapped
    private AnnDetail draft;

    @JsonProperty("draft_id")
    private Long draftId; /* <- nullable，若为 null 则上传新草稿，否则是更新草稿 */
}
