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
import team.project.module.club.management.internal.model.view.selectClubVO;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.department.export.service.DepartmentExportService;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    DepartmentExportService departmentExportService;

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
    public PageVO<selectClubVO> selectByCriteria(ClubInfoQO req) {

        Page<TblClubDO> page = cMapper.selectByCriteria(new Page<>(req.getPageNum(), req.getSize()), req.getDepartmentId(), req.getName());
        List<selectClubVO> list = new ArrayList<>();
        page.getRecords().forEach(club -> {
            selectClubVO vo = new selectClubVO();
            vo.setId(club.getId());
            vo.setIsDeleted(club.getIsDeleted());
            vo.setUpdateTime(club.getUpdateTime());
            vo.setName(club.getName());
            vo.setDepartmentId(club.getDepartmentId());
            vo.setDepartmentName(departmentExportService.getDepartmentName(club.getDepartmentId()));
            vo.setState(club.getState());
            list.add(vo);
        });
        return new PageVO<>(list, new Page<>(req.getPageNum(), req.getSize(), page.getTotal()));
    }

    public int deleteClub(Long departmentId, String name) {
        TblClubDO clubDO = cMapper.selectByNameAndDepartmentId(name, departmentId);
        if (clubDO == null) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到对应的社团信息");
        }
        int result = cMapper.deleteClub(departmentId, name);

        int result2 = tblDutyCirculationMapper.deleteCirculation(clubDO.getId());

        if (result == 0) {
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "删除社团失败");
        }
        if (result2 == 0) {
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "删除社团流转记录失败");
        }

        return pceIService.deleteClubAllMember(clubDO.getId());
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
