package team.project.module.club.management.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class ListClubInfoReq {
    @JsonProperty("departmentId")
    Long     departmentId;

    @Size(max=16, message="社团名字的长度不合约束")
    @JsonProperty("name")
    String   name;

    @Min(value = 1, message="页码不合法")
    Integer  pagenum=1;

    @Min(value = 1, message="每页大小不合法")
    Integer  size=20;
}
