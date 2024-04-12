package team.project.module.club.personnelchanges.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;




@Mapper
public interface TblUserClubMapper extends BaseMapper<TblUserClubDO> {

int setManager(String userId, Long clubId);

int createManager(String userId, Long clubId);

int quashManager(String userId, Long clubId);

Boolean selectManagerRole(String userId, Long clubId);

int createMember(String userId, Long clubId);

int quashMember(String userId, Long clubId);
}
