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

    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;

    @UserIdConstraint
    @JsonProperty("author_id")
    private String authorId;

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
