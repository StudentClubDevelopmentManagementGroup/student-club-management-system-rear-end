package team.project.module.club.personnelchanges.internal.model.query;

import lombok.Data;

@Data
public class ClubQO {

    Long     clubId;

    Integer  pagenum=1;

    Integer  size=20;

    public ClubQO(Long clubId, Integer pagenum, Integer size) {
        this.clubId = clubId;
        this.pagenum = pagenum;
        this.size = size;
    }
}


