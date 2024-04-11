package team.project.module.club.management.internal.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import team.project.base.model.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.project.module.club.management.internal.model.request.TblClubReq;
import team.project.module.club.management.internal.model.datatransfer.ClubMasDTO;

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
    public Page<TblClubDO> selectByNameAndDepartmentId(TblClubReq page) {

        Page<TblClubDO> page2 = cMapper.selectByNameAndDepartmentId(
                new Page<>(page.getPagenum(), page.getSize()),page.getDepartmentId(), page.getName());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
        }
    }

    @Override
    public Page<TblClubDO> selectByName(TblClubReq page) {
        Page<TblClubDO> page2 = cMapper.selectByName(
                new Page<>(page.getPagenum(), page.getSize()), page.getName());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
        }
    }

    public Page<TblClubDO> selectByDepartmentId(TblClubReq page) {
        Page<TblClubDO> page2 = cMapper.selectByDepartmentId(
                new Page<>(page.getPagenum(), page.getSize()), page.getDepartmentId());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
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


    public Page<ClubMasDTO> findAll(TblClubReq page) {
        Page<ClubMasDTO> page1 = new Page<>(page.getPagenum(), page.getSize());
        return cMapper.findAll(page1);
    }

    public Page<ClubMasDTO> findAllByDepartmentId(TblClubReq page) {

        Page<ClubMasDTO> page2 = cMapper.findAllByDepartmentId(
                new Page<>(page.getPagenum(), page.getSize()), page.getDepartmentId());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
        }
    }

    public Page<ClubMasDTO> findAllByName(TblClubReq page) {
        Page<ClubMasDTO> page2 = cMapper.findAllByName(
                new Page<>(page.getPagenum(), page.getSize()), page.getName());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return page2;
        }
    }

    public PageVO<ClubMasDTO> findAllByDepartmentIdAndName(TblClubReq page) {
        Page<ClubMasDTO> page2 = cMapper.findAllByDepartmentIdAndName(
                new Page<>(page.getPagenum(), page.getSize()), page.getDepartmentId(), page.getName());
        if(page2.getTotal()==0){
            throw new ServiceException(ServiceStatus.SUCCESS, "未找到该社团");
        }
        else {
            return new PageVO<>(page2.getRecords(), page2);
        }
    }



}
