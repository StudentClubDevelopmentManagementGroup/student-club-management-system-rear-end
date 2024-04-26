package team.project.module.department.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlterDepartmentNameReq {
    @NotNull
    @JsonProperty("id")
    private Long id;

    @NotNull
    @JsonProperty("fullName")
    private String fullName;
}
