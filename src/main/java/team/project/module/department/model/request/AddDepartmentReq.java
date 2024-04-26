package team.project.module.department.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AddDepartmentReq {
    @JsonProperty("abbreviation")
    @NotNull
    private String abbreviation;

    @JsonProperty("fullName")
    @NotNull
    private String fullName;
}
