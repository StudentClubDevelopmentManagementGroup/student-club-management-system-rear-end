package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

import java.time.LocalDate;

import static team.project.module.club.announcement.internal.model.entity.AnnDO.TITLE_MAX_LENGTH;

@Getter
@Setter
public class AnnSearchReq {

    @Size(max=10, message="社团名检索关键字长度过长")
    @JsonProperty("club_name")
    private String clubName;

    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @UserIdConstraint
    @JsonProperty("author_id")
    private String authorId;

    @Size(max=6, message="作者姓名检索关键字长度过长")
    @JsonProperty("author_name")
    private String authorName;

    /* TODO ljh_TODO 院系编号的校验 */
    @JsonProperty("department_id")
    private Long departmentId;

    @Size(max=TITLE_MAX_LENGTH, message="标题检索关键字长度过长")
    @JsonProperty("title_keyword")
    private String titleKeyword;

    @PastOrPresent(message="按日期查询不能指定将来的日期")
    @JsonProperty("from_date")
    private LocalDate fromDate;

    @PastOrPresent(message="按日期查询不能指定将来的日期")
    @JsonProperty("to_date")
    private LocalDate toDate;
}
