package team.project.module.club.announcement.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AnnouncementDetailVO {

    @JsonProperty("announcement_id")
    private Long announcementId;

    @JsonProperty("club_name")
    private String clubName;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("publish_time")
    private Timestamp publishTime;

    @JsonProperty("update_time")
    private Timestamp updateTime;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;
}
