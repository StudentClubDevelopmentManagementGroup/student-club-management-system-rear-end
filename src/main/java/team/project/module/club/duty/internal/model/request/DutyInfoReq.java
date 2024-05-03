package team.project.module.club.duty.internal.model.request;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;
@Data
public class DutyInfoReq {
   @NonNull
   String    number;

   String    area;

   @NonNull
   Timestamp duty_time;

   @NonNull
   String    arranger_id;

   @NonNull
   String    cleaner_id;

   @NonNull
   Long      club_id;

   @NonNull
   Boolean   ismixed;
}
