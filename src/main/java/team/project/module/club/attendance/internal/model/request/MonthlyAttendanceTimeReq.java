package team.project.module.club.attendance.internal.model.request;

import lombok.Data;
import org.apache.ibatis.annotations.Param;
@Data
public class MonthlyAttendanceTimeReq {

    private String userId;
    private Long clubId;
    private int year;
    private int month;
}
