package team.project.module.club.duty.internal.mapper;

import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.duty.internal.model.entity.TblDutyCirculation;

import java.util.List;

@Mapper
public interface TblDutyCirculationMapper {
    int selectCirculationByClubId(Long clubId);

    int setCirculationByClubId(Long clubId, int circulation);

    int createCirculation(Long clubId);

    int deleteCirculation(Long clubId);

    List<TblDutyCirculation> selectAutoDutyClubid();
}