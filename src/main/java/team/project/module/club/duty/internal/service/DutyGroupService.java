package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;

public interface DutyGroupService extends IService<TblDutyGroup> {
    void createDutyGroup(Long clubId,String memberId,String name);

    void deleteDutyGroup(Long clubId,String memberId,String name);
}
