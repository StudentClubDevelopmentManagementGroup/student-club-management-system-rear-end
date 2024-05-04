package team.project.module.user.internal.model.query;

import lombok.Data;

@Data
public class QueryUserQO {
    private String  userId;
    private String  userName;
    private Long    departmentId;
}
