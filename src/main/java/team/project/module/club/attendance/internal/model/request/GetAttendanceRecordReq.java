package team.project.module.club.attendance.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 查询打卡纪录请求体
 */
@Data
public class GetAttendanceRecordReq {

    @JsonProperty("clubId")
    @NotNull(message = "社团id不能为空")
    private Long clubId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;
}
