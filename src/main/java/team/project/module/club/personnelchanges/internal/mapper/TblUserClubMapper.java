package team.project.module.club.personnelchanges.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.user.internal.model.entity.TblUserDO;

import java.util.List;


@Mapper
public interface TblUserClubMapper extends BaseMapper<TblUserClubDO> {

int setManager(String userId, Long clubId);

int createManager(String userId, Long clubId);

int quashManager(String userId, Long clubId);

Boolean selectManagerRole(String userId, Long clubId);

int createMember(String userId, Long clubId);

int quashMember(String userId, Long clubId);

    default TblUserClubDO selectOne(String userId, Long clubId) {
        List<TblUserClubDO> userList = this.selectList(new LambdaQueryWrapper<TblUserClubDO>()
                .eq(TblUserClubDO::getUserId, userId)
                .eq(TblUserClubDO::getClubId, clubId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }
}
