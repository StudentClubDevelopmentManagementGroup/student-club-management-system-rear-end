package team.project.module.club.attendance.internal.model.view;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttendanceInfoVO {

    private Long id;
    private String userId;
    private Long clubId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkoutTime;
    private boolean isDeleted;

}
