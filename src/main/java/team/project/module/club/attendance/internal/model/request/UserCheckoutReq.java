package team.project.module.club.attendance.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 社团成员签退请求体
 */

@Data
public class UserCheckoutReq {

    @JsonProperty("clubId")
    @NotNull(message = "社团id不能为空")
    private Long clubId;

    @NotNull(message = "用户id不能为空")
    @JsonProperty("userId")
    private String userId;

    @NotNull(message = "签退时间不能为空")
    @JsonProperty("checkoutTime")
    private LocalDateTime checkoutTime;
}
