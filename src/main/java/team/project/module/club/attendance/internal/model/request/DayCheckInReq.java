package team.project.module.club.attendance.internal.model.request;

import lombok.Data;

import java.time.LocalDate;


@Data
public class DayCheckInReq {
    private String userId;
    private Long clubId;
    private LocalDate date;
}
