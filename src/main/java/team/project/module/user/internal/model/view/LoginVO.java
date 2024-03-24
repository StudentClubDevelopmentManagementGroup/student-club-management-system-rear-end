package team.project.module.user.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRole;

@Data
public class LoginVO {
    @JsonProperty("user_id")        private String      userId;
    @JsonProperty("department")     private Department  department;
    @JsonProperty("name")           private String      name;
    @JsonProperty("tel")            private String      tel;
    @JsonProperty("mail")           private String      email;
    @JsonProperty("role")           private Role        role;

    @Data
    public static class Role {
        @JsonProperty("is_student")         private Boolean student;
        @JsonProperty("is_teacher")         private Boolean teacher;
        @JsonProperty("is_club_manager")    private Boolean clubManager;
        @JsonProperty("is_super_admin")     private Boolean superAdmin;
    }

    @Data
    public static class Department {
        @JsonProperty("department_id")      private Long    departmentId;
        @JsonProperty("department_name")    private String  departmentName;
    }
}
