package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.project.base.model.view.PageVO;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;
import team.project.module.club.duty.internal.model.query.DutyGroupQO;

public interface DutyGroupService extends IService<TblDutyGroup> {
    void createDutyGroup(Long clubId,String memberId,String name);

    void deleteDutyGroup(Long clubId,String memberId,String name);

    PageVO<TblDutyGroup> selectDutyGroup(DutyGroupQO qo);
    PageVO<TblDutyGroup> selectDutyGroupByName(DutyGroupQO qo);
    PageVO<TblDutyGroup> selectDutyGroupByGroupName(DutyGroupQO qo);
    PageVO<TblDutyGroup> selectDutyGroupByGroupNameAndName(DutyGroupQO qo);

}
