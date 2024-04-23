package team.project.module.club.management.internal.model.query;

import lombok.Data;

@Data
public class TblClubQO {

    Long     departmentId;

    String   name;

    Integer  pagenum=1;

    Integer  size=20;

    public TblClubQO(Long departmentId, String name, Integer pagenum, Integer size) {
        this.departmentId = departmentId;
        this.name = name;
        this.pagenum = pagenum;
        this.size = size;
    }


    public void setName(String name) {
        this.name = name;
    }
}
