package team.project.module.club.seat.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("tbl_user_club_seat")
public class SeatDO {
    @TableId(value="id")             private Long      seatId;
    @TableField(value="club_id")     private Long      clubId;
    @TableField(value="x")           private Integer   x;
    @TableField(value="y")           private Integer   y;
    @TableField(value="description") private String    description;
    @TableField(value="arranger_id") private String    arrangerId;
    @TableField(value="owner_id")    private String    ownerId;

    public static final int XY_MAX =  32767;
    public static final int XY_MIN = -32767;
    public static final int DESCRIPTION_MAX_LENGTH = 64;
}
