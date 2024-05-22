package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

import java.time.LocalDateTime;

@Data
public class DutyDeleteReq {
    @NotNull
    @JsonProperty("date_time")
    LocalDateTime dateTime;

    @UserIdConstraint
    @NotNull
    @JsonProperty("cleaner_id")
    String        cleanerId;

    @ClubIdConstraint
    @NotNull
    @JsonProperty("club_id")
    Long          clubId;
}
