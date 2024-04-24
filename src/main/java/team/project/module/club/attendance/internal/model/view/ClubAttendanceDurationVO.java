package team.project.module.club.attendance.internal.model.view;

import lombok.Data;

@Data
public class ClubAttendanceDurationVO {
    private String userId;
    private String userName;
    private Long attendanceDurationTime;

}
