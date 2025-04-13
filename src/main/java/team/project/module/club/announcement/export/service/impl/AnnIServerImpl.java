package team.project.module.club.announcement.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.announcement.export.model.datatransfer.AnnDTO;
import team.project.module.club.announcement.export.service.AnnIServer;
import team.project.module.club.announcement.internal.mapper.AnnMapper;
@Service
public class AnnIServerImpl implements AnnIServer {
    @Autowired
    private AnnMapper announcementMapper;
    @Override
    public AnnDTO getNum(Long clubId) {
        return announcementMapper.searchActivity(clubId);
    }
}
