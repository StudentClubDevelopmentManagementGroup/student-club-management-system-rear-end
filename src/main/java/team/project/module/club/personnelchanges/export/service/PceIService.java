package team.project.module.club.personnelchanges.export.service;

import team.project.module.club.personnelchanges.export.model.datatransfer.UserDTO;
import team.project.module.club.personnelchanges.export.model.datatransfer.UserYearlyDTO;

import java.util.List;

public interface PceIService {

    boolean isClubManager(String userId, Long clubId);

    boolean isClubMember(String userId, Long clubId);

    int deleteClubAllMember(Long clubId);

    String getAllClubManagers(Long clubId);
    //获得今年比去年增长的成员数
    UserYearlyDTO getChangeInMembers(Long clubId);

    List<UserDTO> getChangeInMembersByWeek(Long clubId);
}
