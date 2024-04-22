package team.project.module.club.personnelchanges.export.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class ClubReq {


    @ClubIdConstraint
    @JsonProperty("club_id")
    Long     clubId;

    @Min(value = 1, message="页码不合法")
    Integer  pagenum=1;

    @Min(value = 1, message="每页大小不合法")
    Integer  size=20;
}


