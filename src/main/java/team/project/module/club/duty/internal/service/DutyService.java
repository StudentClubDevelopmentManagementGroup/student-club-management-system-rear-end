package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.module.club.duty.internal.model.entity.TblDuty;

import java.sql.Timestamp;


public interface DutyService extends IService<TblDuty> {
    void createDuty(String number, String area, Timestamp dutyTime, String arrangerId
            , String cleanerId, Long club_id, Boolean isMixed);

    void createDutyByGroup(String number, String area, Timestamp dutyTime, String arrangerId
            , String cleanerId, Long clubId, Boolean isMixed, String groupName);
}
