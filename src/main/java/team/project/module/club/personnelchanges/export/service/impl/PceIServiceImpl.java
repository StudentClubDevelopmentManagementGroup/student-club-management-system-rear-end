package team.project.module.club.personnelchanges.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.club.personnelchanges.internal.mapper.TblUserClubMapper;

@Service
public class PceIServiceImpl implements PceIService {

    @Autowired
    TblUserClubMapper tblUserClubMapper;
    public boolean isClubManager(String userId, Long clubId)
    {
        return tblUserClubMapper.selectManagerRole(userId, clubId) != null;
    }
}