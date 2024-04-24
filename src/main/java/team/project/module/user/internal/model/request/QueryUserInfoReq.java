package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import team.project.module.user.export.model.annotation.UserIdConstraint;

import java.util.List;

@Data
public class QueryUserInfoReq {

    @UserIdConstraint
    @JsonProperty("user_id")
    private String userId;

    @Size(min=1, max=5, message="用户姓名长度不和约束")
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("department_id")
    private Long departmentId;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("mail")
    private String email;

    @Pattern(regexp="^(student|teacher)$", message="用户角色只能在“student”和“teacher”中二选一")
    @JsonProperty("role")
    private String role;

    @JsonProperty("l")
    private long l;
}
