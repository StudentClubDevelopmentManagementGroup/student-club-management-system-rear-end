package team.project.module.club.management.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClubReq {
    @NotBlank(message="社团名字不能为空")
    @Size(min=1, max=20, message="社团名字的长度不合约束")
    @JsonProperty("name")
    String   name;


    @NotNull(message="学院id不能为空")
    @JsonProperty("department_id")
    Long     departmentId;


}
