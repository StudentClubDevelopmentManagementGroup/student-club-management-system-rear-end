package team.project.module.club.duty.internal.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DutyGroupQO {
    Long      clubId;

    String    groupName;

    String    name;

    Integer   pageNum;

    Integer   size;
}

