package team.project.module.club.personnelchanges.internal.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserClubInfoVO {
    String clubName;
    Long   clubId;
    String departmentName;
    String role;
}
