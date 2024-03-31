package team.project.module.club.management.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;




@Mapper
public interface TblUserClubMapper extends BaseMapper<TblUserClubDO> {

int setManager(Long userId, Long clubId);

int createManager(Long userId, Long clubId);

int quashManager(Long userId, Long clubId);

int setUserManager(Long userId, Long clubId);

int quashUserManager(Long userId, Long clubId);

}
