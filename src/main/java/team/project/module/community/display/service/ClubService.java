package team.project.module.community.display.service;

import team.project.module.community.display.entity.Club;

import java.util.List;

public interface ClubService {

    List<Club> selectClubsByDepartment(Long id);
}