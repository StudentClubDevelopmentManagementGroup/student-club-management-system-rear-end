package team.project.module.club.seat.tmp;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TmpClubMemberMapper {
    List<String> selectAllMember(Long clubId);
}
