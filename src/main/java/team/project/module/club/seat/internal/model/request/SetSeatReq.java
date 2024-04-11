package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Data
public class SetSeatReq {

    /* TODO jsr303 */
    @JsonProperty("club_id")
    private Long clubId;

    @NotNull(message="未指定座位id")
    @JsonProperty("seat_id")
    private Long seatId;

    @UserIdConstraint
    @JsonProperty("owner_id")
    private String ownerId;
}
