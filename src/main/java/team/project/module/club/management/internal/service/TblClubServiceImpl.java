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
import team.project.module.club.management.internal.model.request.TblClubReq;
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
    public Page<TblClubDO> selectByNameAndDepartmentId(Page<TblClubDO> page, Long departmentId, String name) {
        Page<TblClubDO> page1 = cMapper.selectByNameAndDepartmentId(page,departmentId, name);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }

    @Override
    public Page<TblClubDO> selectByName(Page<TblClubDO> page, String name) {
        Page<TblClubDO> page1 = cMapper.selectByName(page, name);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
        }
    }

    @Override
    public Page<TblClubDO> selectByDepartmentId(Page<TblClubDO> page, Long departmentId) {
        Page<TblClubDO> page1 = cMapper.selectByDepartmentId(page, departmentId);
        if(page1.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page1;
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


    public Page<ClubMasVO> findAll(TblClubReq page) {
        Page<ClubMasVO> page1 = new Page<>(page.getPagenum(), page.getSize());
        return cMapper.findAll(page1);
    }

    public Page<ClubMasVO> findAllByDepartmentId(TblClubReq page) {

        Page<ClubMasVO> page1 = new Page<>(page.getPagenum(), page.getSize());
        Page<ClubMasVO> page2 = cMapper.findAllByDepartmentId(page1, page.getDepartmentId());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
        }
    }

    public Page<ClubMasVO> findAllByName(TblClubReq page) {
        Page<ClubMasVO> page1 = new Page<>(page.getPagenum(), page.getSize());
        Page<ClubMasVO> page2 = cMapper.findAllByName(page1, page.getName());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
        }
    }

    public Page<ClubMasVO> findAllByDepartmentIdAndName(TblClubReq page) {
        Page<ClubMasVO> page1 = new Page<>(page.getPagenum(), page.getSize());
        Page<ClubMasVO> page2 = cMapper.findAllByDepartmentIdAndName(page1, page.getDepartmentId(), page.getName());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
        }
    }



}
