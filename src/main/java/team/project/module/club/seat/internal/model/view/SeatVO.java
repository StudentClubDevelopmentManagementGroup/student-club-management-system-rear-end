package team.project.module.club.seat.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatVO {
    @JsonProperty("seat_id")     private Long    seatId;
    @JsonProperty("description") private String  description;
    @JsonProperty("x")           private Integer x;
    @JsonProperty("y")           private Integer y;
    @JsonProperty("arranger")    private ClubMemberInfoVO arranger;
    @JsonProperty("owner")       private ClubMemberInfoVO owner;
}
