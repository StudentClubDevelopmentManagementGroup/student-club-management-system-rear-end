package team.project.module.club.seat.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;

import java.util.List;

@Mapper
public interface TblUserClubSeatMapper extends BaseMapper<TblUserClubSeatDO> {

    /**
     * 添加新座位
     * */
    default void addSeat(TblUserClubSeatDO seat) {
        if (null == seat.getDescription()) {
            seat.setDescription("");
        }
        this.insert(seat);
    }

    /**
     * 设置座位所属
     * */
    default int setSeatOwner(TblUserClubSeatDO seat) {
        return this.update(null, new LambdaUpdateWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getSeatId, seat.getSeatId())
            .eq(TblUserClubSeatDO::getClubId, seat.getClubId())
            .set(TblUserClubSeatDO::getArrangerId, seat.getArrangerId())
            .set(TblUserClubSeatDO::getOwnerId, seat.getOwnerId()) /* <- nullable */
        );
    }

    /**
     * 更新座位信息（字段传 null 则不更新，且不更新座位所属）
     * */
    default int updateSeatInfo(TblUserClubSeatDO seat) {
        return this.update(null, new LambdaUpdateWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, seat.getClubId())
            .eq(TblUserClubSeatDO::getSeatId, seat.getSeatId())
            .set(seat.getX() != null, TblUserClubSeatDO::getX, seat.getX())
            .set(seat.getY() != null, TblUserClubSeatDO::getY, seat.getY())
            .set(seat.getDescription() != null, TblUserClubSeatDO::getDescription, seat.getDescription())
        );
    }

    /**
     * 查询社团的所有座位
     * */
    default List<TblUserClubSeatDO> selectAll(Long clubId) {
        return this.selectList(null, new LambdaQueryWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, clubId)
        );
    }

    /**
     * 删除座位（真的删除，不是逻辑删除）
     * */
    default int delete(Long clubId, Long seatId) {
        return this.delete(new LambdaQueryWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, clubId)
            .eq(TblUserClubSeatDO::getSeatId, seatId)
        );
    }

    /**
     * 查询非空座位
     * */
    default List<TblUserClubSeatDO> selectOccupiedSeats(Long clubId) {
        return this.selectList(null, new LambdaQueryWrapper<TblUserClubSeatDO>()
            .eq(TblUserClubSeatDO::getClubId, clubId)
            .isNotNull(TblUserClubSeatDO::getOwnerId)
        );
    }
}
