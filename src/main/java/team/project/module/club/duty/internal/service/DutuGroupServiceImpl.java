package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.duty.internal.mapper.TblDutyGroupMapper;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;

public class DutuGroupServiceImpl extends ServiceImpl<TblDutyGroupMapper, TblDutyGroup> implements DutyGroupService {

    @Autowired
    TblDutyGroupMapper tblDutyGroupMapper;
    @Override
    public void createDutyGroup(Long clubId,String memberId,String name) {
        if(tblDutyGroupMapper.selectByClubIdAndMemberIdAndName(clubId, memberId, name)==null){
            tblDutyGroupMapper.createDutyGroup(clubId, memberId, name);
        }else{
            throw new ServiceException(ServiceStatus.CONFLICT, "重复创建");
        }
    }

    @Override
    public void deleteDutyGroup(Long clubId, String memberId, String name) {
        if(tblDutyGroupMapper.selectByClubIdAndMemberIdAndName(clubId, memberId, name)==null){
            throw new ServiceException(ServiceStatus.CONFLICT, "找不到目标");
        }else{
            tblDutyGroupMapper.deleteDutyGroup(clubId, memberId, name);
        }
    }


}
