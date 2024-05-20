package team.project.module.club.management.export.service;

import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;

public interface ManagementIService {
    ClubBasicMsgDTO selectClubBasicMsg(long clubId);

    Long selectClubIdByName(String clubName);
}




