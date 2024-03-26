package team.project.module.community.display.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.community.display.model.entity.Club;
import team.project.module.community.display.mapper.ClubMapper;
import team.project.module.community.display.service.ClubService;

import java.util.List;
@Service
public class ClubServiceImpl implements ClubService {
    @Autowired
    private ClubMapper club;

    public ClubServiceImpl(ClubMapper club) {

        this.club=club;
    }
    @Override
    public List<Club> selectClubsByDepartment(Long id){

        List<Club> clubs = club.selectClubsByDepartment(id);
        return clubs;
    }
}
