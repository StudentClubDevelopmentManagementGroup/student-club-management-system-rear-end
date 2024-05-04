package team.project.module.club.management.export.servivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.department.service.DepartmentService;

@Service
public class managementIServiceImpl implements managementIService {
    @Autowired
    TblClubMapper tblClubMapper;

    @Autowired
    DepartmentService departmentService;

    @Override
    public ClubBasicMsgDTO selectClubBasicMsg(Long clubId) {
        TblClubDO tblClubDO = tblClubMapper.selectById(clubId);
        ClubBasicMsgDTO clubBasicMsgDTO = new ClubBasicMsgDTO();
        clubBasicMsgDTO.setName(tblClubDO.getName());
        clubBasicMsgDTO.setDepartmentName(departmentService.getDepartmentName(tblClubDO.getDepartmentId()));
        return clubBasicMsgDTO;
    }
}
