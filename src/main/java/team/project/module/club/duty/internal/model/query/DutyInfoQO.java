package team.project.module.club.duty.internal.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DutyInfoQO {
    Long      clubId;

    String    number;

    String    name;

    Integer   pageNum;

    Integer   size;
}
