package team.project.module.community.display.model.request;

import lombok.Data;

@Data
public class AlterDepartmentNameReq {
    private String abbreviation;
    private String fullName;
}
