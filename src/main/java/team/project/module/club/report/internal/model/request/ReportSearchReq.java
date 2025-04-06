package team.project.module.club.report.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSearchReq {
    @NotNull
    @JsonProperty("club_id")
    Long clubId;

    @NotNull
    @JsonProperty("keyword")
    String keyword;

    @NotNull
    @JsonProperty("page_num")
    Integer pageNum;

    @NotNull
    @JsonProperty("page_size")
    Integer pageSize;
}
