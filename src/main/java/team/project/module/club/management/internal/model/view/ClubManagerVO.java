package team.project.module.club.management.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/* VO (View Object) 视图对象
   用于封装“以返回给前端用作数据展示的”响应字段 */
@Data
public class ClubManagerVO {
    @JsonProperty("user_id")
    Long   userId;
    @JsonProperty("club_id")
    Long     clubId;
}
