package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

import java.sql.Timestamp;
@Data
public class DutyInfoReq {
   @NotBlank
   String    number;

   String    area;

   @NotNull
   Timestamp duty_time;

   @UserIdConstraint
   @NotNull
   String    arranger_id;

   @UserIdConstraint
   @NotNull
   String    cleaner_id;

   @ClubIdConstraint
   @NotNull
   Long      club_id;

   @NotNull
   Boolean   is_mixed;
}
