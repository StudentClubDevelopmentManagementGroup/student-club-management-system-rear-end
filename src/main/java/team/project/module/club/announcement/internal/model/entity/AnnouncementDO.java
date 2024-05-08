package team.project.module.club.announcement.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@TableName("tbl_club_announcement")
public class AnnouncementDO {
    @TableId(value="id")               private Long      announcementId;
    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")    private Boolean   deleted;
    @TableField(value="create_time")   private Timestamp publishTime; /* create_time 也是 publish_time */
    @TableField(value="update_time")   private Timestamp updateTime;
    @TableField(value="author_id")     private String    authorId;
    @TableField(value="club_id")       private Long      clubId;
    @TableField(value="title")         private String    title;
    @TableField(value="summary")       private String    summary;
    @TableField(value="text_file")     private String    textFile;

    public static final int TitleMaxLength = 127;
    public static final int SummaryMaxLength = 190;
}
