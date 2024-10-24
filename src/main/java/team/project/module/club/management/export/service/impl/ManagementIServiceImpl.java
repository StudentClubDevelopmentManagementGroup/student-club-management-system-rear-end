package team.project.module.club.management.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.export.service.ManagementIService;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.department.internal.service.DepartmentService;

@Service
public class ManagementIServiceImpl implements ManagementIService {
    @Autowired
    TblClubMapper tblClubMapper;

    @Autowired
    DepartmentService departmentService;
    //TODO 优化空返回异常
    @Override
    public ClubBasicMsgDTO selectClubBasicMsg(long clubId) {
        TblClubDO tblClubDO = tblClubMapper.mySelectById(clubId);
        if(tblClubDO==null){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "没有该社团");
        }
        ClubBasicMsgDTO clubBasicMsgDTO = new ClubBasicMsgDTO();
        clubBasicMsgDTO.setName(tblClubDO.getName());
        clubBasicMsgDTO.setDepartmentName(departmentService.getDepartmentName(tblClubDO.getDepartmentId()));
        return clubBasicMsgDTO;
    }

}
