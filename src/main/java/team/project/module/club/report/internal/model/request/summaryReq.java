package team.project.module.club.report.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class summaryReq {
    @NotNull
    @JsonProperty("club_id")
    Long clubId;

    @NotNull(message = "开始时间不能为空")
    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @JsonProperty("endTime")
    private LocalDateTime endTime;
}
