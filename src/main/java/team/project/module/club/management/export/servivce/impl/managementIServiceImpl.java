package team.project.module.club.management.export.servivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.export.servivce.managementIService;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.department.service.DepartmentService;

@Service
public class managementIServiceImpl implements managementIService {
    @Autowired
    TblClubMapper tblClubMapper;

    @Autowired
    DepartmentService departmentService;

    @Override
    public ClubBasicMsgDTO selectClubBasicMsg(long clubId) {
        TblClubDO tblClubDO = tblClubMapper.selectById(clubId);
        if (tblClubDO == null || tblClubDO.getIsDeleted()) {
            throw new ServiceException(ServiceStatus.SUCCESS, "查无对象，或已经删除");
        } else {
            ClubBasicMsgDTO clubBasicMsgDTO = new ClubBasicMsgDTO();
            clubBasicMsgDTO.setName(tblClubDO.getName());
            clubBasicMsgDTO.setDepartmentName(departmentService.getDepartmentName(tblClubDO.getDepartmentId()));
            return clubBasicMsgDTO;
        }

    }
}
