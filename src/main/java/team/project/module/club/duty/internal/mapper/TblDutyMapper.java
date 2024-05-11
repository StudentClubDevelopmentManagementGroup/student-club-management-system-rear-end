package team.project.module.club.duty.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.duty.internal.model.entity.TblDuty;

import java.sql.Timestamp;

@Mapper
public interface TblDutyMapper extends BaseMapper<TblDuty> {
    int createDuty(String number, String area, Timestamp duty_time,
                   String arranger_id, String cleaner_id, Long club_id, Boolean ismixed);

    TblDuty selectOne(String number, String area, Timestamp duty_time,
                      String arranger_id, String cleaner_id, Long club_ide);
}
