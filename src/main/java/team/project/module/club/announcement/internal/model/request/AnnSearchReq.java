package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

import static team.project.module.club.announcement.internal.model.entity.AnnDO.TitleMaxLength;

@Getter
@Setter
public class AnnSearchReq {

    @ClubIdConstraint
    @JsonProperty("club_id")
    Long clubId;

    @JsonProperty("title_keyword")
    @Size(max=TitleMaxLength, message="标题检索关键字长度过长")
    String titleKeyword;
}
