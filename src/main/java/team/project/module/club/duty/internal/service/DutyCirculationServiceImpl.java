package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.duty.internal.mapper.TblDutyCirculationMapper;
import team.project.module.club.duty.internal.model.entity.TblDutyCirculation;

@Service
public class DutyCirculationServiceImpl extends ServiceImpl<TblDutyCirculationMapper, TblDutyCirculation> implements DutyCirculationService{

    @Autowired
    TblDutyCirculationMapper tblDutyCirculationMapper;
    @Override
    public TblDutyCirculation selectCirculationByClubId(Long clubId) {
        TblDutyCirculation tblDutyCirculation = tblDutyCirculationMapper.selectCirculationByClubId(clubId);
        if(tblDutyCirculation == null){
            throw new RuntimeException("查无此社团");
        }
        return tblDutyCirculation;
    }
}
