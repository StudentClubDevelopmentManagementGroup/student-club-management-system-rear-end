package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import java.time.LocalDateTime;

@Data
public class DutyFileUpload {

    @NotNull
    @JsonProperty("date_time")
    LocalDateTime         dateTime;

    @NotBlank
    @JsonProperty("member_id")
    String                memberId;

    @NotNull
    @ClubIdConstraint
    @JsonProperty("club_id")
    Long                  clubId;

    @NotNull
    MultipartFile[]       file;
}
