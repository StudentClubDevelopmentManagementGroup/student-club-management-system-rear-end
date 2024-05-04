package team.project.module.club.personnelchanges.internal.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClubQO {
    Long     clubId;
    Integer  pagenum;
    Integer  size;
}
