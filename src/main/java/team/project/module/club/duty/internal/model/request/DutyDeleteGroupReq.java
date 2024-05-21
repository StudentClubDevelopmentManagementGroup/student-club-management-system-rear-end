package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import java.time.LocalDateTime;

@Data
public class DutyDeleteGroupReq {
    @NotNull
    LocalDateTime dateTime;

    @ClubIdConstraint
    @NotNull
    Long          clubId;

    @NotBlank
    String        groupName;
}
