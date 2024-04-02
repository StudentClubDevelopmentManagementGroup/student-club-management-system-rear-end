package team.project.module.club.management.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class TblClubReq {
    @JsonProperty("departmentId")
    Long     departmentId;
    @JsonProperty("name")
    String   name;
    Integer  pagenum=1;
    Integer  size=20;
}
