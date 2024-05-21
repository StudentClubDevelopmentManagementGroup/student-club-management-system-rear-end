package team.project.module.club.duty.internal.model.view;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class DutyInfoVO {
    Long      id;
    Boolean   deleted;
    Timestamp updateTime;
    String    number;
    String    area;
    Timestamp duty_time;
    String    arranger_id;
    String    arranger_name;
    String    cleaner_id;
    String    cleaner_name;
    Long      club_id;
    String    image_file;
    Boolean   is_mixed;
}
