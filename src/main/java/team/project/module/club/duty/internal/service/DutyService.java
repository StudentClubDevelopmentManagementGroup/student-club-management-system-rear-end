package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.module.club.duty.internal.model.entity.TblDuty;

import java.sql.Timestamp;

@Service
public interface DutyService extends IService<TblDuty> {
    void createDuty(String number,String area,Timestamp duty_time,String arranger_id
            ,String cleaner_id,Long club_id,Boolean ismixed);
}
