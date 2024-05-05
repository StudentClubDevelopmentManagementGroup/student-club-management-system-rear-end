package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import java.util.List;

import static team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO.*;

@Getter
@Setter
public class AddSeatReq {

    @NotNull(message="未指定社团id")
    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @NotEmpty(message="“新增座位”列表不能为空")
    @JsonProperty("seat_list")
    private List<ToAddSeat> seatList;

    @Getter
    @Setter
    static public class ToAddSeat {
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
}