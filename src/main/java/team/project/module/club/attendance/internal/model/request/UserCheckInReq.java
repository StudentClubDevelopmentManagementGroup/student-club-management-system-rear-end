package team.project.module.club.attendance.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.time.LocalDateTime;


@Data
public class UserCheckInReq {
    /* TODO jsr303 */
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("clubId")
    private Long clubId;

    @JsonProperty("checkInTime")
    private LocalDateTime checkInTime;
}
