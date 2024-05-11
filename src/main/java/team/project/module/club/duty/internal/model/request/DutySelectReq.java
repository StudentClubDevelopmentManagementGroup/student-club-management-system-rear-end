package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DutySelectReq {
    @NotBlank
    Long      club_id;

    String    number;

    String    name;

    @Min(value = 1, message="页码不合法")
    Integer pagenum = 1;

    @Min(value = 1, message="每页大小不合法")
    Integer size = 20;
}
