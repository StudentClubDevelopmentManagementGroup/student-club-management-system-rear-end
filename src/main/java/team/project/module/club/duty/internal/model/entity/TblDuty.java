package team.project.module.club.duty.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tbl_user_club_duty")
public class TblDuty {
    @TableId(value="id")               Long      id;
    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")    Boolean   deleted;
    @TableField(value="create_time")   LocalDateTime createTime;
    @TableField(value="update_time")   LocalDateTime updateTime;

    @TableField(value="number")        String    number;
    @TableField(value="area")          String    area;
    @TableField(value="duty_time")     LocalDateTime duty_time;
    @TableField(value="arranger_id")   String    arranger_id;
    @TableField(value="cleaner_id")    String    cleaner_id;
    @TableField(value="club_id")       Long      club_id;
    @TableField(value="image_file")    String    image_file;
    @TableField(value="is_mixed")      Boolean   is_mixed;
}
