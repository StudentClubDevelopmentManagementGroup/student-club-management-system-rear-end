package team.project.module.community.display.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.module.community.display.entity.Club;

import java.util.List;
@Mapper
public interface ClubMapper extends BaseMapper<Club> {
    List<Club> selectClubsByDepartment(Long id);
}
