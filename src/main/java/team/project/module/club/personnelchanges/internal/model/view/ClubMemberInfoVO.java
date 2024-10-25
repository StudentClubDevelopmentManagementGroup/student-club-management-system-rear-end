package team.project.module.club.personnelchanges.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClubMemberInfoVO {
    @JsonProperty("user_id")    private String         userId;
    @JsonProperty("department") private DepartmentInfo department;
    @JsonProperty("name")       private String         name;
    @JsonProperty("tel")        private String         tel;
    @JsonProperty("mail")       private String         email;
    @JsonProperty("role")       private UserRoleInfo   role;

    @Data
    public static class UserRoleInfo {
        @JsonProperty("is_student")      private Boolean student;
        @JsonProperty("is_teacher")      private Boolean teacher;
        @JsonProperty("is_club_member")  private Boolean clubMember;
        @JsonProperty("is_club_manager") private Boolean clubManager;
    }

    @Data
    public static class DepartmentInfo {
        @JsonProperty("department_id")   private Long    departmentId;
        @JsonProperty("department_name") private String  departmentName;
    }
}
