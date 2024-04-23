package team.project.module.club.attendance.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 社团成员签退请求体
 */

@Data
public class UserCheckoutReq {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("clubId")
    private Long clubId;

    @JsonProperty("checkoutTime")
    private LocalDateTime checkoutTime;
}
