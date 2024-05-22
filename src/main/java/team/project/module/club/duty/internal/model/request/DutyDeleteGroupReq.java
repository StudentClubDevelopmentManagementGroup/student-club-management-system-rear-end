package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import java.time.LocalDateTime;

@Data
public class DutyDeleteGroupReq {
    @NotNull
    @JsonProperty("duty_time")
    LocalDateTime dateTime;

    @ClubIdConstraint
    @NotNull
    @JsonProperty("club_id")
    Long          clubId;

    @NotBlank
    @JsonProperty("group_name")
    String        groupName;
}
