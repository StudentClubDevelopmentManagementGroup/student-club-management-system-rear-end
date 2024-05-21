package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DutyFileUpload {

    @NotNull
    LocalDateTime         dateTime;

    @NotBlank
    String                memberId;

    @NotBlank
    Long                  clubId;

    @NotNull
    List<MultipartFile>   file;
}
