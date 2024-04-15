package team.project.module.club.management.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class TblClubReq {

    @JsonProperty("departmentId")
    Long     departmentId;

    @Size( max=16, message="社团名字的长度不合约束")
    @JsonProperty("name")
    String   name;

    Integer  pagenum=1;
    Integer  size=20;
}
