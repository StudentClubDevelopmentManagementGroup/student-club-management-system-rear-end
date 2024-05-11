package team.project.module.club.duty.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.duty.internal.model.entity.TblDuty;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface TblDutyMapper extends BaseMapper<TblDuty> {
    int createDuty(String number, String area, Timestamp duty_time,
                   String arranger_id, String cleaner_id, Long club_id, Boolean ismixed);

    TblDuty selectOne(String number, String area, Timestamp duty_time,
                      String arranger_id, String cleaner_id, Long club_ide);

    int deleteDuty(Timestamp dutyTime, String cleaner_id, Long club_id);

    List<TblDuty> selectLastWeek(Long club_id);

    int setDutyPicture(Timestamp dutyTime, String memberId, Long clubId, String fileId);

    Page<TblDuty> selectDuty(Page<TblDuty> page, Long clubId);
}
