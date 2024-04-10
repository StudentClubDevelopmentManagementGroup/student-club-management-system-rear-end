package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddSeatReq {

    @NotBlank(message="未填写座位描述")
    @Size(max=64, message="座位描述字数过多")
    @JsonProperty("seat")
    private String seat;

    @NotNull(message="未指定社团")
    @JsonProperty("club_id")
    private Long clubId;
}
