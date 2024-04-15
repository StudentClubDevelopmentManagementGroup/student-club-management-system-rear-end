package team.project.module.club.personnelchanges.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Data
public class ClubManagerReq {
    @UserIdConstraint
    @JsonProperty("user_id")
    String   userId;

    @ClubIdConstraint
    @JsonProperty("club_id")
    Long     clubId;
}


