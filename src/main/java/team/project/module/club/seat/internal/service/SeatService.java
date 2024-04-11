package team.project.module.club.seat.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.seat.internal.mapper.TblUserClubSeatMapper;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;
import team.project.module.club.seat.internal.model.request.AddSeatReq;
import team.project.module.club.seat.tmp.service.UserClubService;

@Service
public class SeatService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserClubService userClubService; /* <- tmp */

    @Autowired
    TblUserClubSeatMapper userClubSeatMapper;

    public void addSeat(String arrangerId, AddSeatReq req) {
        if ( ! userClubService.isClubManager(arrangerId, req.getClubId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该社团的负责人");
        }

        TblUserClubSeatDO seatInfo = new TblUserClubSeatDO();
        seatInfo.setArrangerId(arrangerId);
        seatInfo.setSeat(req.getSeat());
        seatInfo.setClubId(req.getClubId());

        try {
            userClubSeatMapper.insert(seatInfo);
        }
        catch (Exception e) {
            logger.error("创建座位失败：", e);
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "创建座位失败");
        }
    }
}
