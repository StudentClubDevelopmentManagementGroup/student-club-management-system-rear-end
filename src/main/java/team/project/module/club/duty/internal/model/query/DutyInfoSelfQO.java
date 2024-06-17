package team.project.module.club.duty.internal.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DutyInfoSelfQO {
    Long      clubId;

    Integer   pageNum;

    Integer   size;
}
