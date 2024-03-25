package team.project.module.club.management.internal.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.project.module.club.management.internal.model.view.ClubMasVO;

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
    TblClubMapper tmplMapper;  /* 示例 */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void createClub(Long departmentId, String name) {

        if(tmplMapper.findByNameAndDepartmentId(departmentId,name).isEmpty()){
            tmplMapper.createClub(departmentId, name);
        }else{
            throw new ServiceException(ServiceStatus.CONFLICT, "已存在同院同名社团");
        }
    }



    @Override
    public Page<TblClubDO> selectByNameAndDepartmentId(Page<TblClubDO> page, Long departmentId, String name) {
        Page<TblClubDO> page1 = tmplMapper.selectByNameAndDepartmentId(page,departmentId, name);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }

    @Override
    public Page<TblClubDO> selectByName(Page<TblClubDO> page, String name) {
        Page<TblClubDO> page1 = tmplMapper.selectByName(page, name);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }

    @Override
    public Page<TblClubDO> selectByDepartmentId(Page<TblClubDO> page, Long departmentId) {
        Page<TblClubDO> page1 = tmplMapper.selectByDepartmentId(page, departmentId);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }

    public void deleteClub(Long departmentId, String name) {
            int result = tmplMapper.deleteClub(departmentId, name);
            if (result == 0) {
                throw new ServiceException(ServiceStatus.SUCCESS, "删除失败");
            }
    }


    public void reuseClub(Long departmentId, String name) {
            int result = tmplMapper.reuseClub(departmentId, name);
            if(result==0){
                throw new ServiceException(ServiceStatus.SUCCESS, "修改失败");
            }
    }

    @Override
    public void deactivateClub(Long departmentId, String name) {
        int result = tmplMapper.deactivateClub(departmentId, name);
        if(result==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
    }

    @Override
    public void recoverClub(Long departmentId, String name) {
        int result = tmplMapper.recoverClub(departmentId, name);
        if(result==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
    }


    public Page<ClubMasVO> findAll(Page<ClubMasVO> page) {
        return tmplMapper.findAll(page);
    }

    public Page<ClubMasVO> findAllByDepartmentId(Page<ClubMasVO> page,Long departmentId) {
        Page<ClubMasVO> page1 = tmplMapper.findAllByDepartmentId(page, departmentId);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }

    public Page<ClubMasVO> findAllByName(Page<ClubMasVO> page,String name) {
        Page<ClubMasVO> page1 = tmplMapper.findAllByName(page, name);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }

    public Page<ClubMasVO> findAllByDepartmentIdAndName(Page<ClubMasVO> page,Long departmentId,String name) {
        Page<ClubMasVO> page1 = tmplMapper.findAllByDepartmentIdAndName(page, departmentId, name);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }



}
