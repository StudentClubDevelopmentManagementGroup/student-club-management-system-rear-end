package team.project.module.club.personnelchanges.export.model.request;

import lombok.Data;

@Data
public class ClubReq {
    Long     clubId;
    Integer  pagenum=1;
    Integer  size=20;
}


