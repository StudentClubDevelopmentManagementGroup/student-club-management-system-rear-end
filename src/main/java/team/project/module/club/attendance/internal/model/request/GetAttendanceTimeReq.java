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


    @JsonProperty("clubName")
    @NotNull(message = "社团名称不能为空")
    private String clubName;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userId")
    private String userId;



    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;



}
