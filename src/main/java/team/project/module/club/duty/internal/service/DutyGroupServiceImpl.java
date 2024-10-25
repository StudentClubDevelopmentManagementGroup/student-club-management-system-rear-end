package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.duty.internal.mapper.TblDutyCirculationMapper;
import team.project.module.club.duty.internal.mapper.TblDutyGroupMapper;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;
import team.project.module.club.duty.internal.model.query.DutyGroupQO;
import team.project.module.club.duty.internal.model.view.DutyGroupSelectVO;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.service.UserInfoServiceI;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DutyGroupServiceImpl extends ServiceImpl<TblDutyGroupMapper, TblDutyGroup> implements DutyGroupService {

    @Autowired
    TblDutyGroupMapper tblDutyGroupMapper;

    @Autowired
    TblDutyCirculationMapper tblDutyCirculationMapper;

    @Autowired
    UserInfoServiceI userInfoServiceI;

    @Override
    public void createDutyGroup(Long clubId, String memberId, String name) {
        if (tblDutyGroupMapper.selectByClubIdAndMemberIdAndName(clubId, memberId, name) == null) {
            tblDutyGroupMapper.createDutyGroup(clubId, memberId, name);
            tblDutyCirculationMapper.setCirculationByClubId(clubId, 1);
        } else {
            throw new ServiceException(ServiceStatus.CONFLICT, "重复创建");
        }
    }

    @Override
    public void deleteDutyGroup(Long clubId, String memberId, String name) {
        if (tblDutyGroupMapper.selectByClubIdAndMemberIdAndName(clubId, memberId, name) == null) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "找不到目标");
        } else {
            tblDutyGroupMapper.deleteDutyGroup(clubId, memberId, name);
            tblDutyCirculationMapper.setCirculationByClubId(clubId, 1);
        }
    }

    @Override
    public PageVO<DutyGroupSelectVO> selectDutyGroup(DutyGroupQO qo) {
        Page<TblDutyGroup> page = tblDutyGroupMapper.selectGroup(new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId());
        if (page.getRecords().size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此社团");
        }
        List<DutyGroupSelectVO> dutyList = new ArrayList<>();
        getDutyGroupSelectVO(dutyList, page);
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize(), page.getTotal()));
    }

    @Override
    public PageVO<DutyGroupSelectVO> selectDutyGroupByName(DutyGroupQO qo) {
        List<UserBasicInfoDTO> nameList = userInfoServiceI.searchUser(qo.getName());
        if (nameList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        List<DutyGroupSelectVO> dutyList = new ArrayList<>();
        for (UserBasicInfoDTO userBasicInfoDTO : nameList) {
            Page<TblDutyGroup> page = tblDutyGroupMapper.selectGroupByName(new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId(), userBasicInfoDTO.getUserId());
            if (page.getRecords().size() == 0) {
                continue;
            }
            getDutyGroupSelectVO(dutyList, page);
        }
        if (dutyList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize(),dutyList.size()));
    }

    @Override
    public PageVO<DutyGroupSelectVO> selectDutyGroupByGroupName(DutyGroupQO qo) {
        Page<TblDutyGroup> page = tblDutyGroupMapper.selectGroupByGroupName(new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId(), qo.getGroupName());
        if (page.getRecords().size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此社团");
        }
        List<DutyGroupSelectVO> dutyList = new ArrayList<>();
        getDutyGroupSelectVO(dutyList, page);
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), page.getSize(), page.getTotal()));
    }

    @Override
    public PageVO<DutyGroupSelectVO> selectDutyGroupByGroupNameAndName(DutyGroupQO qo) {
        List<UserBasicInfoDTO> nameList = userInfoServiceI.searchUser(qo.getName());
        if (nameList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        List<DutyGroupSelectVO> dutyList = new ArrayList<>();
        for (UserBasicInfoDTO userBasicInfoDTO : nameList) {
            Page<TblDutyGroup> page = tblDutyGroupMapper.selectGroupByNameAndGroupName(new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId(), userBasicInfoDTO.getUserId(), qo.getGroupName());
            if (page.getRecords().size() == 0) {
                continue;
            }
            getDutyGroupSelectVO(dutyList, page);
        }
        if (dutyList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize(),dutyList.size()));
    }

    private void getDutyGroupSelectVO(List<DutyGroupSelectVO> dutyList, Page<TblDutyGroup> page) {
        page.getRecords().forEach(tblDutyGroup -> {
            DutyGroupSelectVO dutyGroupSelectVO = new DutyGroupSelectVO();
            dutyGroupSelectVO.setId(tblDutyGroup.getId());
            dutyGroupSelectVO.setClubId(tblDutyGroup.getClubId());
            dutyGroupSelectVO.setMemberId(tblDutyGroup.getMemberId());
            dutyGroupSelectVO.setName(tblDutyGroup.getName());
            dutyGroupSelectVO.setUserName(userInfoServiceI.getUserName(tblDutyGroup.getMemberId()));
            dutyList.add(dutyGroupSelectVO);
        });
    }


}
