package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;

@Service
public interface DutyGroupService extends IService<TblDutyGroup> {
    void createDutyGroup(Long clubId,String memberId,String name);

    void deleteDutyGroup(Long clubId,String memberId,String name);
}
