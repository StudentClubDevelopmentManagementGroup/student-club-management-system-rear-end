package team.project.module.club.duty.internal.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DutyInfoWithTimeQO {
    Long      clubId;

    String    number;

    String    name;

    LocalDateTime dutyTime;

    Integer   pageNum;

    Integer   size;


}
