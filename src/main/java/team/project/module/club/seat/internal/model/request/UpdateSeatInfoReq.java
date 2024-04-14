package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import static team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO.*;

@Data
public class UpdateSeatInfoReq {

    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @NotNull(message="未指定座位id")
    @JsonProperty("seat_id")
    private Long seatId;

    @Max(value=xyMax, message="坐标值x不合约束")
    @Min(value=xyMin, message="坐标值x不合约束")
    @JsonProperty("x")
    private Integer x;

    @Max(value=xyMax, message="坐标值y不合约束")
    @Min(value=xyMin, message="坐标值y不合约束")
    @JsonProperty("y")
    private Integer y;

    @Size(max=descriptionMaxLength, message="座位描述字数过多")
    @JsonProperty("description")
    private String description;
}