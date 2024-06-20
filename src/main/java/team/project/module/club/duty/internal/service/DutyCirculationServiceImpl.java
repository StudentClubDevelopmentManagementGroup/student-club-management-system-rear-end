package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
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
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无数据");
        }
        return tblDutyCirculation;
    }

    @Override
    public void setCirculationByClubId(Long clubId, int circulation) {
        TblDutyCirculation tblDutyCirculation = tblDutyCirculationMapper.selectCirculationByClubId(clubId);
        if(tblDutyCirculation == null){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无数据");
        }
        int result = tblDutyCirculationMapper.setCirculationByClubId(clubId, circulation);
        if(result == 0){
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "修改失败");
        }
    }
}
