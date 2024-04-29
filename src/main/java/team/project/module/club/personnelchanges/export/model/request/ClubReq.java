package team.project.module.club.personnelchanges.export.model.request;

import lombok.Data;

/** review by ljh 2024-04-29 TODO 改类名字
 * */
@Data
public class ClubReq {
    Long     clubId;
    Integer  pagenum=1;
    Integer  size=20;
}
