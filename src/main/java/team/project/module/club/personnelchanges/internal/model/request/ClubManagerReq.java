package team.project.module.club.personnelchanges.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Data
public class ClubManagerReq {
    @UserIdConstraint
    @JsonProperty("user_id")
    String   userId;
    @NonNull
    @JsonProperty("club_id")
    Long     clubId;


}


