package team.project.module.club.duty.internal.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblDutyCirculationMapper {
    int selectCirculationByClubId(Long clubId);

    int setCirculationByClubId(Long clubId, int circulation);

    int createCirculation(Long clubId);

    int deleteCirculation(Long clubId);
}
