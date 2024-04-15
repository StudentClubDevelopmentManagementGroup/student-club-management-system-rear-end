package team.project.module.community.display.model.request.department;

import lombok.Data;

@Data
public class AlterDepartmentNameReq {
    private Long id;
    private String fullName;
}
