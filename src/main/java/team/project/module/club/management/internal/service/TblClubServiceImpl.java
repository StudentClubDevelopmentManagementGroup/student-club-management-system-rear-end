package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.duty.internal.mapper.TblDutyCirculationMapper;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.query.ClubInfoQO;
import team.project.module.club.personnelchanges.export.service.PceIService;

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

    @Autowired
    PceIService pceIService;

    @Autowired
    TblDutyCirculationMapper tblDutyCirculationMapper;

    public void createClub(Long departmentId, String name) {
        if (cMapper.findByNameAndDepartmentId(departmentId, name).isEmpty()) {
            cMapper.createClub(departmentId, name);
            int i = tblDutyCirculationMapper.createCirculation(cMapper.selectByNameAndDepartmentId(name, departmentId).getId());
            if (i == 0) {
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "创建基地值日循环表数据失败");
            }
        } else {
            throw new ServiceException(ServiceStatus.CONFLICT, "已存在同院同名社团");
        }
    }

    @Override
    public PageVO<TblClubDO> selectByCriteria(ClubInfoQO req) {

        Page<TblClubDO> page = cMapper.selectByCriteria(new Page<>(req.getPageNum(), req.getSize()), req.getDepartmentId(), req.getName());

        if (page.getTotal() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团");
        } else {
            return new PageVO<>(page);
        }
    }

    public int deleteClub(Long departmentId, String name) {
        int result = cMapper.deleteClub(departmentId, name);
        int result2 = tblDutyCirculationMapper.deleteCirculation(cMapper.selectByNameAndDepartmentId(name, departmentId).getId());
        if (result == 0) {
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "删除失败");
        }
        if (result2 == 0) {
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "基地值日循环表数据删除失败");
        }
        TblClubDO club = cMapper.selectByNameAndDepartmentId(name, departmentId);
        return pceIService.deleteClubAllMember(club.getId());
    }

    public void reuseClub(Long departmentId, String name) {
        int result = cMapper.reuseClub(departmentId, name);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "修改失败");
        }
    }

    @Override
    public void deactivateClub(Long departmentId, String name) {
        int result = cMapper.deactivateClub(departmentId, name);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团");
        }
    }

    @Override
    public void recoverClub(Long departmentId, String name) {
        int result = cMapper.recoverClub(departmentId, name);
        if (result == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团");
        }
    }

    public PageVO<ClubMsgDTO> findAll(ClubInfoQO req) {
        Page<ClubMsgDTO> page1 = new Page<>(req.getPageNum(), req.getSize());
        page1 = cMapper.findAll(page1, req.getDepartmentId(), req.getName());
        return new PageVO<>(page1);
    }

}
