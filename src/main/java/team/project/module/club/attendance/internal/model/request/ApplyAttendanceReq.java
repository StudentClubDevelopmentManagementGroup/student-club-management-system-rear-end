package team.project.module.club.attendance.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplyAttendanceReq {

    @NotNull(message = "用户id不能为空")
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("clubName")
    @NotNull(message = "社团名称不能为空")
    private String clubName;

    @NotNull(message = "签到时间不能为空")
    @JsonProperty("checkInTime")
    private LocalDateTime checkInTime;

    @NotNull(message = "签退时间不能为空")
    @JsonProperty("checkoutTime")
    private LocalDateTime checkoutTime;


}
