package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
@Data
public class DutyFileUpload {

    @NotNull
    Timestamp       duty_time;

    @NotBlank
    String          member_id;

    @NotBlank
    Long            club_id;

    @NotNull
    MultipartFile   file;
}
