package team.project.module.club.management.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnClubInfoReq {
    @NotNull
    @JsonProperty("club_id")
    Long clubId;

    @Min(value = 1, message="页码不合法")
    @JsonProperty("page_num")
    Integer pageNum = 1;

    @Min(value = 1, message="每页大小不合法")
    @JsonProperty("page_size")
    Integer size = 20;
}
