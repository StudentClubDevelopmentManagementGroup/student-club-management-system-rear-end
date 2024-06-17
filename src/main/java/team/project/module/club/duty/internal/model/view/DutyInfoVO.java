package team.project.module.club.duty.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DutyInfoVO {
    @JsonProperty("id")
    Long          id;
    @JsonProperty("is_deleted")
    Boolean       deleted;
    @JsonProperty("update_time")
    LocalDateTime updateTime;
    @JsonProperty("number")
    String        number;
    @JsonProperty("area")
    String        area;
    @JsonProperty("date_time")
    LocalDateTime dateTime;
    @JsonProperty("arranger_id")
    String        arrangerId;
    @JsonProperty("arranger_name")
    String        arrangerName;
    @JsonProperty("cleaner_id")
    String        cleanerId;
    @JsonProperty("cleaner_name")
    String        cleanerName;
    @JsonProperty("club_id")
    Long          clubId;
    @JsonProperty("image_file")
    List<String> imageFile;
    @JsonProperty("is_mixed")
    Boolean       isMixed;
}
