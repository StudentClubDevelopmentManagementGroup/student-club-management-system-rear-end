package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import java.util.List;

@Data
public class AddSeatReq {

    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @NotEmpty(message="“新增座位”列表不能为空")
    @JsonProperty("seat_list")
    private List<SeatInfo> seatList;

    @Data
    static public class SeatInfo {
        @Max(value=65535, message="坐标值不合约束")
        @Min(value=-65535, message="坐标值不合约束")
        @JsonProperty("x")
        private Integer x;

        @Max(value=65535, message="坐标值不合约束")
        @Min(value=-65535, message="坐标值不合约束")
        @JsonProperty("y")
        private Integer y;

        @NotBlank(message="未填写座位描述")
        @Size(max=64, message="座位描述字数过多")
        @JsonProperty("description")
        private String description;
    }
}
