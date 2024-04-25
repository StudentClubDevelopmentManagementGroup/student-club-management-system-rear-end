package team.project.module.club.management.internal.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClubInfoQO {

    Long     departmentId;

    String   name;

    Integer  pagenum=1;

    Integer  size=20;
}
