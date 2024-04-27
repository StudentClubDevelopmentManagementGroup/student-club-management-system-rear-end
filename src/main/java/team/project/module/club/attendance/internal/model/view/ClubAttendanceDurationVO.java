package team.project.module.club.attendance.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClubAttendanceDurationVO {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("attendanceDurationTime")
    private Long attendanceDurationTime;

}
