package team.project.module.community_display.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.community_display.mapper.ClubMapper;
import team.project.module.community_display.entity.Club;
import team.project.module.community_display.service.ClubService;

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
        //return club.selectClubsByDepartment( id);
        List<Club> clubs = club.selectClubsByDepartment(id); // 假设这里使用了 MyBatis 进行数据库查询
        System.out.println("Clubs for department " + id + ": " + clubs); // 打印查询到的指定院系的所有社团信息
        return clubs;
    }
}
