package team.project.module.club.seat.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tbl_user_club_seat")
public class TblUserClubSeatDO {

    @TableId(value="id")             private Long      id;
    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")  private Boolean   deleted;
    @TableField(value="create_time") private Timestamp createTime;
    @TableField(value="update_time") private Timestamp updateTime;

    @TableField(value="arranger_id") private String    arrangerId;
    @TableField(value="seat")        private String    seat;
    @TableField(value="owner_id")    private String    ownerId;
    @TableField(value="club_id")     private Long      clubId;
}
