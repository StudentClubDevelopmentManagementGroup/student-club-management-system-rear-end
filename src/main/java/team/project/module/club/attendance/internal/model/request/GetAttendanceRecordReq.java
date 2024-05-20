package team.project.module.club.attendance.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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


    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;

    @JsonProperty("currentPage")
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页必须大于等于1")
    private Integer currentPage;

    @JsonProperty("pageSize")
    @NotNull(message = "页大小不能为空")
    @Min(value = 1, message = "页大小必须大于等于1")
    private Integer pageSize;

}
