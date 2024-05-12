package team.project.module.club.duty.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class TblDutyCirculation {
    @TableId(value="id")                   Long       id;
    @TableField(value="club_id")           Long       club_id;
    @TableLogic(value="0", delval="1")
    @TableField(value="is_circulation")    Boolean    is_circulation;
}