package team.project.module.club.management.export.servivce;

import team.project.module.club.management.internal.model.datatransfer.ClubBasicMsgDTO;

public interface managementIService {
    ClubBasicMsgDTO selectClubBasicMsg(Long clubId);
}
