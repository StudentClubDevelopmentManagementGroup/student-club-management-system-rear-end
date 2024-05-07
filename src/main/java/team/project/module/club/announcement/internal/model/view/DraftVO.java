package team.project.module.club.announcement.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DraftVO {

    @JsonProperty("draft_id")
    private Long draftId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;
}
