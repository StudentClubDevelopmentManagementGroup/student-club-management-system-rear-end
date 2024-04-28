package team.project.module.club.seat.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfoVO {
    @JsonProperty("user_id")    private String  userId;
    @JsonProperty("name")       private String  name;
    @JsonProperty("is_student") private Boolean student;
    @JsonProperty("is_teacher") private Boolean teacher;
}