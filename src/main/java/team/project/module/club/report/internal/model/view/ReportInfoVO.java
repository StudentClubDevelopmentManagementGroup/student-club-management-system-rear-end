package team.project.module.club.report.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportInfoVO {
    @JsonProperty("id")
    Long               id;
    @JsonProperty("deleted")
    Boolean            deleted;
    @JsonProperty("create_time")
    LocalDateTime      createTime;
    @JsonProperty("update_time")
    LocalDateTime      updateTime;
    @JsonProperty("uploader_id")
    String             uploader;
    @JsonProperty("club_id")
    Long               clubId;
    @JsonProperty("report_file")
    List<String>       reportFile;
    @JsonProperty("report_type")
    String             reportType;
}
