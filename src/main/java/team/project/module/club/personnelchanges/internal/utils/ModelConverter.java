package team.project.module.club.personnelchanges.internal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.personnelchanges.internal.model.datatransfer.ClubMemberInfoDTO;
import team.project.module.club.personnelchanges.internal.model.view.ClubMemberInfoVO;
import team.project.module.department.service.DepartmentService;

import java.util.ArrayList;
import java.util.List;

import static team.project.module.user.export.model.enums.UserRole.STUDENT;
import static team.project.module.user.export.model.enums.UserRole.TEACHER;

@Component("club-personnelchanges-ModelConverter")
public class ModelConverter {

    @Autowired
    private DepartmentService departmentService;

    public List<ClubMemberInfoVO> toClubMemberInfoVOList(List<ClubMemberInfoDTO> clubMembers) {

        List<ClubMemberInfoVO> result = new ArrayList<>();

        for (ClubMemberInfoDTO clubMemberInfoDTO : clubMembers) {

            ClubMemberInfoVO.UserRoleInfo role = new ClubMemberInfoVO.UserRoleInfo();
            role.setClubMember(clubMemberInfoDTO.isMember());
            role.setClubManager(clubMemberInfoDTO.isManager());
            role.setStudent(clubMemberInfoDTO.hasRole(STUDENT));
            role.setTeacher(clubMemberInfoDTO.hasRole(TEACHER));

            ClubMemberInfoVO.DepartmentInfo departmentInfo = new ClubMemberInfoVO.DepartmentInfo();
            departmentInfo.setDepartmentId(clubMemberInfoDTO.getDepartmentId());
            departmentInfo.setDepartmentName(departmentService.getDepartmentName(clubMemberInfoDTO.getDepartmentId()));

            ClubMemberInfoVO clubMemberInfo = new ClubMemberInfoVO();
            clubMemberInfo.setRole(role);
            clubMemberInfo.setUserId(clubMemberInfoDTO.getUserId());
            clubMemberInfo.setDepartment(departmentInfo);
            clubMemberInfo.setName(clubMemberInfoDTO.getName());
            clubMemberInfo.setTel(clubMemberInfoDTO.getTel());
            clubMemberInfo.setEmail(clubMemberInfoDTO.getEmail());

            result.add(clubMemberInfo);
        }

        return result;
    }
}
