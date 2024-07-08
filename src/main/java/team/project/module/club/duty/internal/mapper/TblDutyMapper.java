package team.project.module.club.duty.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.duty.internal.model.entity.TblDuty;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TblDutyMapper extends BaseMapper<TblDuty> {
    int createDuty(String number, String area, LocalDateTime dateTime, String arrangerId, String cleanerId, Long clubId, Boolean isMixed);

    TblDuty selectOne(String number, String area, LocalDateTime dateTime, String arrangerId, String cleanerId, Long clubId);

    int deleteDuty(LocalDateTime dateTime, String cleanerId, Long clubId);

    List<TblDuty> selectLastWeek(Long clubId);

    List<TblDuty> selectNextWeek(Long clubId);

    int setDutyPicture(LocalDateTime dateTime, String memberId, Long clubId, String fileId);

    Page<TblDuty> selectDuty(Page<TblDuty> page, Long clubId);

    Page<TblDuty> selectDutyByNumber(Page<TblDuty> page, Long clubId, String number);

    Page<TblDuty> selectDutyByName(Page<TblDuty> page, Long clubId, String cleanerId);

    Page<TblDuty> selectDutyByNumberAndName(Page<TblDuty> page, Long clubId, String cleanerId, String name,String number);

    Page<TblDuty> selectDutyByUserId(Page<TblDuty> page, String userId);

    List<TblDuty> selectDutyTomorrow();

    List<TblDuty> selectDutyTodayNotFinished();
}
