package team.project.module.club.announcement.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class UploadAnnouncementReq {
    @NotNull(message="未指定社团id")
    @ClubIdConstraint
    @JsonProperty("club_id")
    private Long clubId;
}