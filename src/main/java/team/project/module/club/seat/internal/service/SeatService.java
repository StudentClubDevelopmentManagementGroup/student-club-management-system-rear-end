package team.project.module.club.seat.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.club.seat.internal.mapper.TblUserClubSeatMapper;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;
import team.project.module.club.seat.internal.model.request.AddSeatReq;
import team.project.module.club.seat.internal.model.request.DelSeatReq;
import team.project.module.club.seat.internal.model.request.SetOwnerReq;
import team.project.module.club.seat.internal.model.request.UpdateSeatInfoReq;
import team.project.module.club.seat.internal.model.view.SeatVO;
import team.project.module.club.seat.internal.util.ModelConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PceIService clubMemberRoleService;

    @Autowired
    TblUserClubSeatMapper seatMapper;

    @Autowired
    ModelConverter modelConverter;

   @Transactional
    public void addSeat(String arrangerId, AddSeatReq req) {
        if ( ! clubMemberRoleService.isClubManager(arrangerId, req.getClubId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "座位安排者不是该社团的负责人");
        }

        List<TblUserClubSeatDO> seatsToAdd = new ArrayList<>();
        for (AddSeatReq.SeatInfo seatInfo : req.getSeatList()) {

            TblUserClubSeatDO seat = new TblUserClubSeatDO();
            seat.setX(seatInfo.getX());
            seat.setY(seatInfo.getY());
            seat.setDescription(seatInfo.getDescription());
            seat.setArrangerId(arrangerId);
            seat.setClubId(req.getClubId());

            seatsToAdd.add(seat);
        }

        for (TblUserClubSeatDO seat : seatsToAdd) { /* 可优化，但无所谓了，毕竟添加座位不是频繁的操作 */
            seatMapper.addSeat(seat);
        }
    }

    public void setOwner(String arrangerId, SetOwnerReq req) {

        Long clubId = req.getClubId();
        String ownerId = req.getOwnerId();

        if ( ! clubMemberRoleService.isClubManager(arrangerId, clubId)) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "座位安排者不是该社团的负责人");
        }
        /* TODO
        if (ownerId != null &&  ! clubMemberRoleService.isClubMember(ownerId, clubId)) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "座位所属者不是该社团的成员");
        } */

        TblUserClubSeatDO seat = new TblUserClubSeatDO();
        seat.setClubId(clubId);
        seat.setSeatId(req.getSeatId());
        seat.setArrangerId(arrangerId);
        seat.setOwnerId(ownerId);

        int result = seatMapper.setSeatOwner(seat);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "分配座位失败");
        }
    }

    public void updateSeatInfo(String arrangerId, UpdateSeatInfoReq req) {
        if ( ! clubMemberRoleService.isClubManager(arrangerId, req.getClubId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "座位安排者不是该社团的负责人");
        }

        TblUserClubSeatDO seat = new TblUserClubSeatDO();
        seat.setClubId(req.getClubId());
        seat.setSeatId(req.getSeatId());
        seat.setArrangerId(arrangerId);
        seat.setX(req.getX());
        seat.setY(req.getY());
        seat.setDescription(req.getDescription());

        int result = seatMapper.updateSeatInfo(seat);
    }

    public List<SeatVO> view(String userId, Long clubId) {
        /* TODO
        if ( ! clubMemberRoleService.isClubMember(userId, clubId)) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该社团的成员");
        }*/

        List<TblUserClubSeatDO> seatList = seatMapper.selectAll(clubId);

        List<SeatVO> result = new ArrayList<>();
        for (TblUserClubSeatDO seat : seatList) {
            result.add(modelConverter.toSeatVO(seat));
        }

        return result;
    }

    public void deleteSeat(String arrangerId, DelSeatReq req) {
        if ( ! clubMemberRoleService.isClubManager(arrangerId, req.getClubId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "座位安排者不是该社团的负责人");
        }

        int result = seatMapper.delete(req.getClubId(), req.getSeatId());
        if (1 != result) {
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "删除座位失败");
        }
    }
}
