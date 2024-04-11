package team.project.module.club.personnelchanges.export.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

@Data
public class ClubManagerReq {

    @JsonProperty("user_id")
    String   userId;
    @JsonProperty("club_id")
    Long     clubId;


}


