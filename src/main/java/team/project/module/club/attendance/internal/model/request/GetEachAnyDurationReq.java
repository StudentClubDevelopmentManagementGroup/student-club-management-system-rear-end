package team.project.module.club.attendance.internal.model.request;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 查询社团每个成员指定时间段打卡时长请求体
 */

@Data
public class GetEachAnyDurationReq {

    private Long clubId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
