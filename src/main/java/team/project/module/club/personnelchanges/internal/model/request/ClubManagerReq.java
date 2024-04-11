package team.project.module.club.personnelchanges.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClubManagerReq {

    @JsonProperty("user_id")
    String   userId;
    @JsonProperty("club_id")
    Long     clubId;


}


