package team.project.module.user.internal.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserInfoQO {
    private String userId;
    private String userName;
    private Long   departmentId;
}
