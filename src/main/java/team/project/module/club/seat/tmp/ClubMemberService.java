package team.project.module.club.seat.tmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("club-seat-tmp-ClubMemberService")
public class ClubMemberService {

    @Autowired
    TmpClubMemberMapper mapper;

    public List<String> allMember(Long clubId) {
        return mapper.selectAllMember(clubId);
    }
}
