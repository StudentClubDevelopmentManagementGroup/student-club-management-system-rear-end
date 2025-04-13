package team.project.module.club.announcement.export.service;

import team.project.module.club.announcement.export.model.datatransfer.AnnDTO;

public interface AnnIServer {

    public AnnDTO getNum(Long clubId);
}
