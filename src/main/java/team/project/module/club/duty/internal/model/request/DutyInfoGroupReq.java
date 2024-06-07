package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

import java.time.LocalDateTime;

@Data
public class DutyInfoGroupReq {
    @NotBlank
    @JsonProperty("number")
    String        number;

    @JsonProperty("area")
    String        area;

    @NotNull
    @JsonProperty("date_time")
    LocalDateTime dateTime;

    @UserIdConstraint
    @JsonProperty("arranger_id")
    String        arrangerId;


    @ClubIdConstraint
    @NotNull
    @JsonProperty("club_id")
    Long          clubId;

    @NotNull
    @JsonProperty("is_mixed")
    Boolean       isMixed;

    @NotBlank
    @JsonProperty("group_name")
    String        groupName;
}
