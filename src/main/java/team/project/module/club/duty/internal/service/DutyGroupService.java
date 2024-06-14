package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.project.base.model.view.PageVO;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;
import team.project.module.club.duty.internal.model.query.DutyGroupQO;
import team.project.module.club.duty.internal.model.view.DutyGroupSelectVO;

public interface DutyGroupService extends IService<TblDutyGroup> {
    void createDutyGroup(Long clubId,String memberId,String name);

    void deleteDutyGroup(Long clubId,String memberId,String name);

    PageVO<DutyGroupSelectVO> selectDutyGroup(DutyGroupQO qo);
    PageVO<DutyGroupSelectVO> selectDutyGroupByName(DutyGroupQO qo);
    PageVO<DutyGroupSelectVO> selectDutyGroupByGroupName(DutyGroupQO qo);
    PageVO<DutyGroupSelectVO> selectDutyGroupByGroupNameAndName(DutyGroupQO qo);

}
