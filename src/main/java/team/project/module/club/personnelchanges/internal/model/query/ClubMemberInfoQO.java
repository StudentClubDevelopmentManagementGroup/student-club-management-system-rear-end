package team.project.module.club.personnelchanges.internal.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClubMemberInfoQO {

    Long     departmentId;

    String   name;

    Long     clubId;

    Integer  pagenum=1;

    Integer  size=20;
}


