package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

import java.time.LocalDateTime;

@Data
public class DutyDeleteReq {
    @NotNull
    LocalDateTime duty_time;

    @UserIdConstraint
    @NotNull
    String    cleaner_id;

    @ClubIdConstraint
    @NotNull
    Long      club_id;
}
