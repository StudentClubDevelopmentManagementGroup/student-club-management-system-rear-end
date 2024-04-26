package team.project.module.club.personnelchanges.internal.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public List<ClubMemberInfoVO> toClubMemberInfoVOList(Page<ClubMemberInfoDTO> clubMembers) {
        List<ClubMemberInfoVO> result = new ArrayList<>();
        clubMembers.getRecords().forEach(clubMember ->
        {
            ClubMemberInfoVO.UserRoleInfo ui = new ClubMemberInfoVO.UserRoleInfo();
            ClubMemberInfoVO.DepartmentInfo departmentInfo = new ClubMemberInfoVO.DepartmentInfo();
            ClubMemberInfoVO clubMemberInfo = new ClubMemberInfoVO();

            ui.setClubMember(clubMember.isMember());
            ui.setClubManager(clubMember.isManager());
            ui.setStudent(clubMember.hasRole(STUDENT));
            ui.setTeacher(clubMember.hasRole(TEACHER));

            departmentInfo.setDepartmentId(clubMember.getDepartmentId());
            departmentInfo.setDepartmentName(departmentService.getDepartmentName(clubMember.getDepartmentId()));

            clubMemberInfo.setRole(ui);
            clubMemberInfo.setUserId(clubMember.getUserId());
            clubMemberInfo.setDepartment(departmentInfo);
            clubMemberInfo.setName(clubMember.getName());
            clubMemberInfo.setTel(clubMember.getTel());
            clubMemberInfo.setEmail(clubMember.getEmail());
            result.add(clubMemberInfo);
        });
        return result;
    }
}
