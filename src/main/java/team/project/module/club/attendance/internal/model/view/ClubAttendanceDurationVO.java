package team.project.module.club.attendance.internal.model.view;

import lombok.Data;

@Data
public class ClubAttendanceDurationVO {
    private String userId;
    Long totalMonthDuration;
    String totalMonthDurationHMS;
}
