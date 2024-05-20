package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class DutySelectReq {
    @NotNull
    @ClubIdConstraint
    Long      club_id;

    String    number;

    String    name;

    @Min(value = 1, message="页码不合法")
    Integer pagenum = 1;

    @Min(value = 1, message="每页大小不合法")
    Integer size = 20;
}
