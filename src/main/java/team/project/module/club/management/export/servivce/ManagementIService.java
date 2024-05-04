package team.project.module.club.management.export.servivce;

import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;

public interface ManagementIService {
    ClubBasicMsgDTO selectClubBasicMsg(long clubId);
}
