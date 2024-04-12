package team.project.module.club.seat.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;

import java.util.List;

@Mapper
public interface TblUserClubSeatMapper extends BaseMapper<TblUserClubSeatDO> {

    default void setSeat(TblUserClubSeatDO seat) {
        this.update(null, new LambdaUpdateWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getId, seat.getId())
            .eq(TblUserClubSeatDO::getClubId, seat.getClubId())
            .set(TblUserClubSeatDO::getArrangerId, seat.getArrangerId())
            .set(TblUserClubSeatDO::getOwnerId, seat.getOwnerId())
        );
    }

    default List<TblUserClubSeatDO> selectAllSeat(Long clubId) {
        return this.selectList(null, new LambdaQueryWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, clubId)
        );
    }

    default int deleteSeat(Long clubId, Long seatId) {
        return this.delete(new LambdaQueryWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getId, seatId)
            .eq(TblUserClubSeatDO::getClubId, clubId)
        );
    }
}
