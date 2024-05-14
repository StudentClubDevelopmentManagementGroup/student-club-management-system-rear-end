package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.project.module.club.duty.internal.model.entity.TblDutyCirculation;
public interface DutyCirculationService extends IService<TblDutyCirculation> {
    TblDutyCirculation selectCirculationByClubId(Long clubId);
}
