package team.project.module.club.personnelchanges.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.export.service.ManagementIService;
import team.project.module.club.personnelchanges.internal.mapper.TblUserClubMapper;
import team.project.module.club.personnelchanges.internal.model.datatransfer.ClubMemberInfoDTO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.internal.model.query.ClubMemberInfoQO;
import team.project.module.club.personnelchanges.internal.model.query.ClubQO;
import team.project.module.club.personnelchanges.internal.model.view.ClubMemberInfoVO;
import team.project.module.club.personnelchanges.internal.model.view.UserClubInfoVO;
import team.project.module.club.personnelchanges.internal.utils.ModelConverter;
import team.project.module.user.export.service.UserInfoServiceI;

import java.util.ArrayList;
import java.util.List;

import static team.project.module.user.export.model.enums.UserRole.CLUB_MANAGER;
import static team.project.module.user.export.model.enums.UserRole.CLUB_MEMBER;

/**
 * <p style="color: #f23215;">
 * 基地表 服务实现类
 * </p>
 *
 * @author LIn
 * @since 2024-03-17
 */
@Service
public class TblUserClubServiceImpl extends ServiceImpl<TblUserClubMapper, TblUserClubDO> implements TblUserClubService {

    @Autowired
    TblUserClubMapper ucMapper;

    @Autowired
    UserInfoServiceI uiService;

    @Autowired
    ModelConverter converter;

    @Autowired
    ManagementIService managementIService;

    @Transactional
    public void setClubManager(String userId, Long clubId) {
        TblUserClubDO user = ucMapper.selectOne(userId, clubId);
        if (user == null) {
            ucMapper.createManager(userId, clubId);
            uiService.addRoleToUser(userId, CLUB_MANAGER);
        } else {
            ucMapper.setManager(userId, clubId);
            uiService.addRoleToUser(userId, CLUB_MANAGER);
        }
    }

    @Transactional
    @Override
    public void quashClubManager(String userId, Long clubId) {
        TblUserClubDO user = ucMapper.selectOne(userId, clubId);
        if (user == null) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "没有对象");
        } else {
            try {
                ucMapper.quashManager(userId, clubId);
                if (ucMapper.selectRootRole(userId) == null) {
                    uiService.removeRoleFromUser(userId, CLUB_MANAGER);
                }
            } catch (Exception e) {
                // FIXME 待修改状态码422 UNPROCESSABLE_ENTITY
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "删除失败");
            }
        }
    }

    @Transactional
    @Override
    public void createMember(String userId, Long clubId) {
        TblUserClubDO user = ucMapper.selectOne(userId, clubId);
        if (user == null) {
            try {
                ucMapper.createMember(userId, clubId);
                uiService.addRoleToUser(userId, CLUB_MEMBER);
            } catch (Exception a) {
                // FIXME 待修改状态码422 UNPROCESSABLE_ENTITY
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "创建失败");
            }
        } else {
            throw new ServiceException(ServiceStatus.CONFLICT, "已经存在");
        }
    }

    @Transactional
    @Override
    public void quashMember(String userId, Long clubId) {
        TblUserClubDO user = ucMapper.selectOne(userId, clubId);
        if (user == null) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "没有对象");
        } else {
            try {
                ucMapper.quashMember(userId, clubId);
                if (ucMapper.selectMemberRole(userId) == null) {
                    uiService.removeRoleFromUser(userId, CLUB_MEMBER);
                }
            } catch (Exception a) {
                // FIXME 待修改状态码422 UNPROCESSABLE_ENTITY
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "删除失败");
            }
        }
    }

    public PageVO<UserMsgDTO> selectClubMember(ClubQO req) {
        Page<UserMsgDTO> user = ucMapper.selectClubMember(new Page<>(req.getPagenum(), req.getSize()), req.getClubId());
        return new PageVO<>(user);
    }

    public Boolean selectTheMember(String userId, Long clubId) {
        TblUserClubDO user = ucMapper.selectOne(userId, clubId);
        return user != null;
    }

    public PageVO<ClubMemberInfoVO> selectClubMemberInfo(ClubMemberInfoQO req) {
        Page<ClubMemberInfoDTO> page = ucMapper.selectClubMemberInfo(new Page<>(req.getPagenum(), req.getSize()), req.getClubId(), req.getName(), req.getDepartmentId());
        List<ClubMemberInfoVO> result = converter.toClubMemberInfoVOList(page.getRecords());
        return new PageVO<>(result, page);
    }

    @Override
    public List<UserClubInfoVO> selectMemberAllClubInfo(String userId) {
        List<TblUserClubDO> list = ucMapper.selectOneAllClubInfo(userId);
        if (list.isEmpty()) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无对象");
        }
        List<UserClubInfoVO> result = new ArrayList<>();
        for (TblUserClubDO tblUserClubDO : list) {
            if (tblUserClubDO != null) {
                ClubBasicMsgDTO clubBasicMsgDTO = managementIService.selectClubBasicMsg(tblUserClubDO.getClubId());
                UserClubInfoVO userClubInfoVO = new UserClubInfoVO(null, null, null, null);
                userClubInfoVO.setClubId(tblUserClubDO.getClubId());
                switch (tblUserClubDO.getRole()) {
                    case 1 -> userClubInfoVO.setRole("成员");
                    case 2, 3 -> userClubInfoVO.setRole("管理员");
                    default -> userClubInfoVO.setRole("未知");
                }
                userClubInfoVO.setClubName(clubBasicMsgDTO.getName());
                userClubInfoVO.setDepartmentName(clubBasicMsgDTO.getDepartmentName());
                result.add(userClubInfoVO);
            }
        }
        return result;
    }
}
