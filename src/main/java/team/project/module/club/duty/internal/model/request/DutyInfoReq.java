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
   Timestamp dutyTime;

   @UserIdConstraint
   @NotNull
   String    arrangerId;

   @UserIdConstraint
   @NotNull
   String    cleanerId;

   @ClubIdConstraint
   @NotNull
   Long      clubId;

   @NotNull
   Boolean   isMixed;
}