package team.project.module.community_display.service;

import team.project.module.community_display.entity.Club;

import java.util.List;

public interface ClubService {

    List<Club> selectClubsByDepartment(Long id);
}
