package team.project.module.community.display.model.request.attendence;

import lombok.Data;

import java.time.LocalDate;


@Data
public class DayCheckInReq {
    private String userId;
    private Long clubId;
    private LocalDate date;
}
