package team.project.module.club.seat.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tbl_user_club_seat")
public class TblUserClubSeatDO {

    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")  private Boolean   deleted;

    @TableId(value="id")             private Long      seatId;
    @TableField(value="club_id")     private Long      clubId;
    @TableField(value="x")           private Integer   x;
    @TableField(value="y")           private Integer   y;
    @TableField(value="description") private String    description;
    @TableField(value="arranger_id") private String    arrangerId;
    @TableField(value="owner_id")    private String    ownerId;

    public static final int xyMax = 32767;
    public static final int xyMin = -32767;
    public static final int descriptionMaxLength = 64;
}
