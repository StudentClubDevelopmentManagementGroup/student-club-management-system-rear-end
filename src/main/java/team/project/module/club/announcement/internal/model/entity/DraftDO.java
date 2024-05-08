package team.project.module.club.announcement.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@TableName("tbl_club_announcement_draft")
public class DraftDO {
    @TableId(value="id")               private Long      draftId;
    @TableField(value="create_time")   private Timestamp createTime;
    @TableField(value="update_time")   private Timestamp updateTime;
    @TableField(value="author_id")     private String    authorId;
    @TableField(value="club_id")       private Long      clubId;
    @TableField(value="title")         private String    title;
    @TableField(value="summary")       private String    summary;
    @TableField(value="text_file")     private String    textFile;
}
