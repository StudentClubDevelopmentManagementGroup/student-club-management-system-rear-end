package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueryUserInfoReq {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("must_teacher")
    private Boolean mustTeacher = false;

    @JsonProperty("must_student")
    private Boolean mustStudent = false;
}
