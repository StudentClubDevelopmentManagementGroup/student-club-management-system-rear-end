package team.project.module.club.management.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ListClubInfoReq {
    @JsonProperty("department_id")
    Long departmentId;

    @Size(max=16, message="社团名字的长度不合约束")
    @JsonProperty("club_name")
    String  name;

    @Min(value = 1, message="页码不合法")
    @JsonProperty("page_num")
    Integer pageNum = 1;

    @Min(value = 1, message="每页大小不合法")
    @JsonProperty("page_size")
    Integer size = 20;
}
