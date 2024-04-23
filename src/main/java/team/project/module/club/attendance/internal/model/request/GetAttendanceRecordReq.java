package team.project.module.club.attendance.internal.model.request;

import lombok.Data;

import java.time.LocalDateTime;
/**
 * 查询打卡纪录请求体
 */
@Data
public class GetAttendanceRecordReq {

    private String userId;
    private Long clubId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
