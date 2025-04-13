package team.project.module.club.management.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import team.project.module.club.announcement.export.model.datatransfer.AnnDTO;
import team.project.module.club.attendance.export.model.datatransfer.AttDTO;
import team.project.module.club.personnelchanges.export.model.datatransfer.UserDTO;
import team.project.module.club.personnelchanges.export.model.datatransfer.UserYearlyDTO;

import java.util.List;

@Getter
@Setter
public class AiVO {
    @JsonProperty("user_yearly")
    UserYearlyDTO userYearlyDTO;

    @JsonProperty("att_list")
    List<AttDTO> attDTOList;

    @JsonProperty("user_list")
    List<UserDTO> userDTOList;

    @JsonProperty("ann")
    AnnDTO annDTO;

    @JsonProperty("message")
    String message;
}
