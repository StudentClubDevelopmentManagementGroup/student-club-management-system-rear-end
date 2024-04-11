package team.project.module.club.seat.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;

@Mapper
public interface TblUserClubSeatMapper extends BaseMapper<TblUserClubSeatDO> {

    default int updateSeat(TblUserClubSeatDO seat) {
        return this.update(null, new LambdaUpdateWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getId, seat.getId())
            .eq(TblUserClubSeatDO::getClubId, seat.getClubId())
            .set(TblUserClubSeatDO::getArrangerId, seat.getArrangerId())
            .set(TblUserClubSeatDO::getOwnerId, seat.getOwnerId())
        );
    }
}
