package team.project.module.club.report.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReportReq {
    @NotNull
    @JsonProperty("report_type")
    String reportType;

    @NotNull
    @JsonProperty("club_id")
    Long clubId;

    @NotNull
    MultipartFile[]       file;
}
