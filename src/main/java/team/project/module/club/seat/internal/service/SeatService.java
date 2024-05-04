package team.project.module.club.seat.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.club.seat.internal.mapper.TblUserClubSeatMapper;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;
import team.project.module.club.seat.internal.model.request.AddSeatReq;
import team.project.module.club.seat.internal.model.request.DelSeatReq;
import team.project.module.club.seat.internal.model.request.UpdateSeatReq;
import team.project.module.club.seat.internal.model.view.ClubMemberInfoVO;
import team.project.module.club.seat.internal.model.view.SeatVO;
import team.project.module.club.seat.internal.util.ModelConverter;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoIService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    @Autowired
    PceIService clubMemberRoleService;

    @Autowired
    UserInfoIService userInfoService;

    @Autowired
    TblUserClubSeatMapper seatMapper;

    @Autowired
    ModelConverter modelConverter;

    @Transactional
    public List<SeatVO> addSeat(String arrangerId, AddSeatReq req) {
        if ( ! clubMemberRoleService.isClubManager(arrangerId, req.getClubId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "座位安排者不是该社团的负责人");
        }

        List<TblUserClubSeatDO> seatsToAdd = new ArrayList<>();
        for (AddSeatReq.ToAddSeat seatInfo : req.getSeatList()) {

            TblUserClubSeatDO seat = new TblUserClubSeatDO();
            seat.setX(seatInfo.getX());
            seat.setY(seatInfo.getY());
            seat.setDescription(seatInfo.getDescription());
            seat.setArrangerId(arrangerId);
            seat.setClubId(req.getClubId());

            seatsToAdd.add(seat);
        }

        /* 批量添加可优化，但无所谓了，毕竟添加座位不是频繁的操作 */
        for (TblUserClubSeatDO seat : seatsToAdd) {
            seatMapper.addSeat(seat);
        }

        /* 返回添加后的座位信息，携带主键 seat_id */
        List<SeatVO> result = new ArrayList<>();
        for (TblUserClubSeatDO seat : seatsToAdd) {
            result.add(modelConverter.toSeatVO(seat));
        }
        return result;
    }

    @Transactional
    public void updateSeat(String arrangerId, UpdateSeatReq req) {
        if ( ! clubMemberRoleService.isClubManager(arrangerId, req.getClubId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "座位安排者不是该社团的负责人");
        }

        for (UpdateSeatReq.ToUpdateSeat seat : req.getSeatList()) {
            if (seat.getOwnerId() != null && ! clubMemberRoleService.isClubMember(seat.getOwnerId(), req.getClubId())) {
                throw new ServiceException(ServiceStatus.FORBIDDEN, "座位所属者不是该社团的成员");
            }
        }

        /* 批量修改可优化，但无所谓了，毕竟修改座位不是频繁的操作 */
        for (UpdateSeatReq.ToUpdateSeat seatInfo : req.getSeatList()) {

            TblUserClubSeatDO seat = new TblUserClubSeatDO();
            seat.setClubId(req.getClubId());
            seat.setSeatId(seatInfo.getSeatId());
            seat.setArrangerId(arrangerId);

            /* 更新座位信息 */
            if (seatInfo.getX() != null || seatInfo.getY() != null || seatInfo.getDescription() != null) {
                seat.setX(seatInfo.getX());
                seat.setY(seatInfo.getY());
                seat.setDescription(seatInfo.getDescription());

                if (1 != seatMapper.updateSeatInfo(seat)) {
                    throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "更新座位信息失败");
                }
            }

            /* 更新座位所属 */
            boolean isUnsetOwner = seatInfo.getUnsetOwner();
            if (isUnsetOwner || seatInfo.getOwnerId() != null) {
                seat.setOwnerId(isUnsetOwner ? null : seatInfo.getOwnerId());
                if (0 == seatMapper.setSeatOwner(seat)) {
                    throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "分配座位失败");
                }
            }
        }
    }

    public List<SeatVO> view(String userId, Long clubId) {

        if ( ! clubMemberRoleService.isClubMember(userId, clubId)) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该社团的成员");
        }

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

    public PageVO<ClubMemberInfoVO> membersNoSeat(String arrangerId, Long clubId, PagingQueryReq pageReq) {

        if ( ! clubMemberRoleService.isClubManager(arrangerId, clubId)) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该社团的负责人");
        }

        Page<Object> page = Page.of(pageReq.getPageNum(), pageReq.getPageSize());
        List<String> membersId = seatMapper.selectNoSeatMembersId(page, clubId);

        List<ClubMemberInfoVO> result = new ArrayList<>();
        for (String userId : membersId) {
            UserBasicInfoDTO userBasicInfo = userInfoService.selectUserBasicInfo(userId);

            ClubMemberInfoVO memberInfo = new ClubMemberInfoVO();
            memberInfo.setUserId(userId);
            memberInfo.setName(userBasicInfo.getName());
            memberInfo.setStudent(userBasicInfo.hasRole(UserRole.STUDENT));
            memberInfo.setTeacher(userBasicInfo.hasRole(UserRole.TEACHER));

            result.add(memberInfo);
        }

        return new PageVO<>(result, page);
    }
}
