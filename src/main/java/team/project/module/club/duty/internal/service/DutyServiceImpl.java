package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.project.module.club.duty.internal.mapper.TblDutyMapper;
import team.project.module.club.duty.internal.model.entity.TblDuty;

import java.sql.Timestamp;

public class DutyServiceImpl extends ServiceImpl<TblDutyMapper, TblDuty> implements DutyService{

    @Override
    public void createDuty(String number,String area,Timestamp duty_time,String arranger_id
            ,String cleaner_id,Long club_id,Boolean ismixed) {

    }
}
