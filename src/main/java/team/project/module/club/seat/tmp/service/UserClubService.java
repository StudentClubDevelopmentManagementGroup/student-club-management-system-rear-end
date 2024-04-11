package team.project.module.club.seat.tmp.service;

import org.springframework.stereotype.Service;

@Service("tmp-userClubService")
public class UserClubService {

    /* 判断该用户是否是指定社团的负责人 */
    public boolean isClubManager(String userId, Long clubId) {
        return true;
    }
}
