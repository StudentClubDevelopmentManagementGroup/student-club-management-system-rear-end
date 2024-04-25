package team.project.module.club.attendance.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 查询打卡时长请求体
 */
@Data
public class GetAttendanceTimeReq {

    @JsonProperty("clubId")
    @NotNull(message = "社团id不能为空")
    private Long clubId;

    @JsonProperty("userId")
    private String userId;


    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;

    @JsonProperty("currentPage")
    @NotNull(message = "当前页不能为空")
    private Long currentPage;

    @JsonProperty("pageSize")
    @NotNull(message = "页大小不能为空")
    private Long pageSize;

}
