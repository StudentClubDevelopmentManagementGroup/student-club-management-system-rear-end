package team.project.module.department.model.request;

import lombok.Data;

@Data
public class AlterDepartmentNameReq {
    private Long id;
    private String fullName;
}
