package team.project.module.club.seat.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import team.project.base.controller.exception.InvalidParamException;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

import java.util.List;

import static team.project.module.club.seat.internal.model.entity.SeatDO.*;

@Getter
@Setter
public class UpdateSeatReq {

    @NotNull(message="未指定社团id")
    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @NotEmpty(message="“更新座位”列表不能为空")
    @JsonProperty("seat_list")
    List<ToUpdateSeat> seatList;

    @Getter
    @Setter
    public static class ToUpdateSeat {
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

        @UserIdConstraint
        @JsonProperty("owner_id")
        private String ownerId; /* <- nullable */

        @JsonProperty(value="unset_owner", defaultValue="false")
        private Boolean unsetOwner = false; /* <- 是否将座位置空 */
    }

    /* 校验：如果要将座位置空，则不该指明所属者的学号/工号 */
    public static void validate(UpdateSeatReq req) {
        if (req.seatList.stream().anyMatch(seat -> seat.unsetOwner && seat.ownerId != null)) {
            throw new InvalidParamException("如果要将座位置空，则不该指明所属者的学号/工号");
        }
    }
}
