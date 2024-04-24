package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.query.ClubInfoQO;

/**
 * <p style="color: #f23215;">
 * 基地表 服务实现类
 * </p>
 *
 * @author LIn
 * @since 2024-03-17
 */
@Service
public class TblClubServiceImpl extends ServiceImpl<TblClubMapper, TblClubDO> implements TblClubService {
    @Autowired
    TblClubMapper cMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void createClub(Long departmentId, String name) {

        if(cMapper.findByNameAndDepartmentId(departmentId,name).isEmpty()){
            cMapper.createClub(departmentId, name);
        }else{
            throw new ServiceException(ServiceStatus.CONFLICT, "已存在同院同名社团");
        }
    }



    @Override
    public PageVO<TblClubDO> selectByNameAndDepartmentId(ClubInfoQO req) {

        Page<TblClubDO> page = cMapper.selectByNameAndDepartmentId(
                new Page<>(req.getPagenum(), req.getSize()),req.getDepartmentId(), req.getName());
        if(page.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return new PageVO<>(page);
        }
    }

    @Override
    public PageVO<TblClubDO> selectByName(ClubInfoQO req) {
        Page<TblClubDO> page = cMapper.selectByName(
                new Page<>(req.getPagenum(), req.getSize()), req.getName());
        if(page.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return new PageVO<>(page);
        }
    }

    public PageVO<TblClubDO> selectByDepartmentId(ClubInfoQO req) {
        Page<TblClubDO> page = cMapper.selectByDepartmentId(
                new Page<>(req.getPagenum(), req.getSize()), req.getDepartmentId());
        if(page.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return new PageVO<>(page);
        }
    }

    public void deleteClub(Long departmentId, String name) {
            int result = cMapper.deleteClub(departmentId, name);
            if (result == 0) {
                throw new ServiceException(ServiceStatus.SUCCESS, "删除失败");
            }
    }


    public void reuseClub(Long departmentId, String name) {
            int result = cMapper.reuseClub(departmentId, name);
            if(result==0){
                throw new ServiceException(ServiceStatus.SUCCESS, "修改失败");
            }
    }

    @Override
    public void deactivateClub(Long departmentId, String name) {
        int result = cMapper.deactivateClub(departmentId, name);
        if(result==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
    }

    @Override
    public void recoverClub(Long departmentId, String name) {
        int result = cMapper.recoverClub(departmentId, name);
        if(result==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
    }


    public PageVO<ClubMsgDTO> findAll(ClubInfoQO page) {
        Page<ClubMsgDTO> page1 = new Page<>(page.getPagenum(), page.getSize());
        page1 = cMapper.findAll(page1);
        return new PageVO<>(page1);
    }

    public PageVO<ClubMsgDTO> findAllByDepartmentId(ClubInfoQO page) {

        Page<ClubMsgDTO> page2 = cMapper.findAllByDepartmentId(
                new Page<>(page.getPagenum(), page.getSize()), page.getDepartmentId());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return new PageVO<>(page2);
        }
    }

    public PageVO<ClubMsgDTO> findAllByName(ClubInfoQO req) {
        Page<ClubMsgDTO> page = cMapper.findAllByName(
                new Page<>(req.getPagenum(), req.getSize()), req.getName());
        if(page.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return new PageVO<>(page);
        }
    }

    public PageVO<ClubMsgDTO> findAllByDepartmentIdAndName(ClubInfoQO req) {
        Page<ClubMsgDTO> page = cMapper.findAllByDepartmentIdAndName(
                new Page<>(req.getPagenum(), req.getSize()), req.getDepartmentId(), req.getName());
        if(page.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return new PageVO<>(page);
        }
    }


}
