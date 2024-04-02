package team.project.module.club.management.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;




@Mapper
public interface TblUserClubMapper extends BaseMapper<TblUserClubDO> {

int setManager(String userId, Long clubId);

int createManager(String userId, Long clubId);

int quashManager(String userId, Long clubId);


}
