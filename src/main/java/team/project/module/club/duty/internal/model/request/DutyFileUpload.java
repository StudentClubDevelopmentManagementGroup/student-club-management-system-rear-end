package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DutyFileUpload {

    @NotNull
    @JsonProperty("date_time")
    LocalDateTime         dateTime;

    @NotBlank
    @JsonProperty("member_id")
    String                memberId;

    @NotBlank
    @JsonProperty("club_id")
    Long                  clubId;

    @NotNull
    List<MultipartFile>   file;
}
