package team.project.module.club.management.export.service;

import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;

import java.util.List;

public interface ManagementIService {
    ClubBasicMsgDTO selectClubBasicMsg(long clubId);

    List<Long> selectRecruitmentClub();
}
