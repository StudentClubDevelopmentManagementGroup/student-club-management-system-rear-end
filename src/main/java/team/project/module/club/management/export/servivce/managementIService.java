package team.project.module.club.management.export.servivce;

import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;

public interface managementIService {
    ClubBasicMsgDTO selectClubBasicMsg(long clubId);
}
