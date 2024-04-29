package team.project.module.club.attendance.internal.model.view;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttendanceInfoVO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("clubId")
    private Long clubId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("checkInTime")
    private LocalDateTime checkInTime;

    @JsonProperty("checkoutTime")
    private LocalDateTime checkoutTime;

    @JsonProperty("attendanceDuration")
    private Long attendanceDuration;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

}
