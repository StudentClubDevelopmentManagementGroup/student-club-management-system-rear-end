package team.project.module.club.duty.internal.model.view;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DutyInfoVO {
    Long          id;
    Boolean       deleted;
    LocalDateTime updateTime;
    String        number;
    String        area;
    LocalDateTime dateTime;
    String        arrangerId;
    String        arrangerName;
    String        cleanerId;
    String        cleanerName;
    Long          clubId;
    String        imageFile;
    Boolean       isMixed;
}
