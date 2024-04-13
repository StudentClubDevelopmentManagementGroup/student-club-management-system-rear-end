package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class UnsetOwnerReq {

    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @NotNull(message="未指定座位id")
    @JsonProperty("seat_id")
    private Long seatId;
}
