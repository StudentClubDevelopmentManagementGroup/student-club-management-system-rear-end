package team.project.module.club.seat.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;

import java.util.List;

@Mapper
public interface TblUserClubSeatMapper extends BaseMapper<TblUserClubSeatDO> {

    default void addSeat(TblUserClubSeatDO seat) {
        String description = seat.getDescription();
        seat.setDescription(description == null ? "" : description.trim());
        this.insert(seat);
    }

    default int setSeatOwner(TblUserClubSeatDO seat) {
        return this.update(null, new LambdaUpdateWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getSeatId, seat.getSeatId())
            .eq(TblUserClubSeatDO::getClubId, seat.getClubId())
            .set(TblUserClubSeatDO::getArrangerId, seat.getArrangerId())
            .set(TblUserClubSeatDO::getOwnerId, seat.getOwnerId()) /* <- nullable */
        );
    }

    default int updateSeatInfo(TblUserClubSeatDO seat) {
        String description = seat.getDescription();
        if (description != null) {
            description = description.trim();
        }
        return this.update(null, new LambdaUpdateWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, seat.getClubId())
            .eq(TblUserClubSeatDO::getSeatId, seat.getSeatId())
            .set(seat.getX() != null, TblUserClubSeatDO::getX, seat.getX())
            .set(seat.getY() != null, TblUserClubSeatDO::getY, seat.getY())
            .set(description != null, TblUserClubSeatDO::getDescription, description)
        );
    }

    default List<TblUserClubSeatDO> selectAll(Long clubId) {
        return this.selectList(null, new LambdaQueryWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, clubId)
        );
    }

    default int delete(Long clubId, Long seatId) {
        return this.delete(new LambdaQueryWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, clubId)
            .eq(TblUserClubSeatDO::getSeatId, seatId)
        );
    }
}
