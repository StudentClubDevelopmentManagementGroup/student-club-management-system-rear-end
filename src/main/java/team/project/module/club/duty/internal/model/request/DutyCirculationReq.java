package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class DutyCirculationReq {
    @ClubIdConstraint
    @NotNull
    @JsonProperty("club_id")
    private Long clubId;

    @NotNull
    @JsonProperty("circulation")
    private int circulation;
}
