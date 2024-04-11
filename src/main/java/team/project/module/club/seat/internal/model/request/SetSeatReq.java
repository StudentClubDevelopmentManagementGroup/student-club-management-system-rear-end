package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Data
public class SetSeatReq {

    @NotNull(message="未指定座位id")
    @JsonProperty("seat_id")
    private Long seatId;

    @UserIdConstraint
    private Long ownerId;
}
