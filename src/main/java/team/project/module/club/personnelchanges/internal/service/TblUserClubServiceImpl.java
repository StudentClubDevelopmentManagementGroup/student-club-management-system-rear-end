package team.project.module.club.personnelchanges.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.internal.mapper.TblUserClubMapper;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.internal.model.query.ClubQO;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.service.UserInfoIService;

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
    UserInfoIService uiService;
    @Transactional
    public void setClubManager(String userId, Long clubId) {
        TblUserClubDO user =ucMapper.selectOne(userId, clubId);
        if(user==null){
            ucMapper.createManager(userId, clubId);
            uiService.addRoleToUser(userId, CLUB_MANAGER);
        }
        else {
            ucMapper.setManager(userId, clubId);
            uiService.addRoleToUser(userId, CLUB_MANAGER);
        }
    }
    @Transactional
    @Override
    public void quashClubManager(String userId, Long clubId) {
        TblUserClubDO user =ucMapper.selectOne(userId, clubId);
        if(user==null){
            throw new ServiceException(ServiceStatus.SUCCESS, "没有对象");
        }
        else {
            try
            {
                ucMapper.quashManager(userId, clubId);
                if(ucMapper.selectRootROle(userId)==null){
                    uiService.removeRoleFromUser(userId, CLUB_MANAGER);
                }
            }
            catch (Exception e){
                // FIXME 待修改状态码422 UNPROCESSABLE_ENTITY
                throw new ServiceException(ServiceStatus.SUCCESS, "删除失败");
            }
        }

    }
    @Transactional
    @Override
    public void createMember(String userId, Long clubId) {
        TblUserClubDO user =ucMapper.selectOne(userId, clubId);
        if(user==null){
            try
            {
                ucMapper.createMember(userId, clubId);
                uiService.addRoleToUser(userId,CLUB_MEMBER);
            }
            catch (Exception a){
                // FIXME 待修改状态码422 UNPROCESSABLE_ENTITY
                throw new ServiceException(ServiceStatus.SUCCESS, "创建失败");
            }
        }
        else {
            throw new ServiceException(ServiceStatus.SUCCESS, "已经存在");
        }
    }
    @Transactional
    @Override
    public void quashMember(String userId, Long clubId) {
        TblUserClubDO user =ucMapper.selectOne(userId, clubId);
        if(user==null){
            throw new ServiceException(ServiceStatus.SUCCESS, "没有对象");
        }
        else {
            try
            {
                ucMapper.quashMember(userId, clubId);
                if(ucMapper.selectMemberRole(userId)==null){
                    uiService.removeRoleFromUser(userId, CLUB_MEMBER);
                }
            }
            catch (Exception a){
                // FIXME 待修改状态码422 UNPROCESSABLE_ENTITY
                throw new ServiceException(ServiceStatus.SUCCESS, "删除失败");
            }
        }
    }

    public PageVO<UserMsgDTO> selectClubMember(ClubQO req) {
        Page<UserMsgDTO> user =  ucMapper.selectClubMember(
                new Page<>(req.getPagenum(), req.getSize()),req.getClubId());
            return new PageVO<>(user);
    }



    public Boolean selectTheMember(String userId, Long clubId) {
        TblUserClubDO user =ucMapper.selectOne(userId, clubId);
        return user != null;
    }

    public PageVO<UserInfoDTO> selectClubMemberInfo(ClubQO req) {
        Page<UserInfoDTO> user =  ucMapper.selectClubMemberInfo(
                new Page<>(req.getPagenum(), req.getSize()),req.getClubId());
        return new PageVO<>(user);
    }


}
