package team.project.module.club.duty.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TblDutyCirculation {
    @TableId(value="id")                   Long       id;
    @TableField(value="club_id")           Long       club_id;
    @TableField(value="is_circulation")    Boolean    is_circulation;
}
