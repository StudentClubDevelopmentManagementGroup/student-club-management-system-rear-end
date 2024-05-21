package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.duty.internal.mapper.TblDutyCirculationMapper;
import team.project.module.club.duty.internal.mapper.TblDutyGroupMapper;
import team.project.module.club.duty.internal.mapper.TblDutyMapper;
import team.project.module.club.duty.internal.model.entity.TblDuty;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;
import team.project.module.club.duty.internal.model.query.DutyInfoQO;
import team.project.module.club.duty.internal.model.view.DutyInfoVO;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static team.project.module.util.filestorage.export.model.enums.FileStorageType.CLOUD;

@Service
@Slf4j
public class DutyServiceImpl extends ServiceImpl<TblDutyMapper, TblDuty> implements DutyService {

    @Autowired
    TblDutyMapper tblDutyMapper;

    @Autowired
    TblDutyGroupMapper tblDutyGroupMapper;

    @Autowired
    TblDutyCirculationMapper tblDutyCirculationMapper;

    @Autowired
    FileStorageServiceI FileStorageService;

    @Autowired
    UserInfoServiceI UserInfoService;

    @Override
    public void createDuty(String number, String area, LocalDateTime duty_time, String arranger_id, String cleaner_id, Long club_id, Boolean ismixed) {
        if (tblDutyMapper.createDuty(number, area, duty_time, arranger_id, cleaner_id, club_id, ismixed) != 1) {
            throw new ServiceException(ServiceStatus.CONFLICT, "创建失败");
        }
    }

    @Override
    @Transactional
    public void createDutyByGroup(String number, String area, LocalDateTime dutyTime, String arrangerId, String cleanerId, Long clubId, Boolean isMixed, String groupName) {
        List<TblDutyGroup> dutyGroupList = tblDutyGroupMapper.selectUserIdByGroupName(clubId, groupName);
        for (TblDutyGroup tblDutyGroup : dutyGroupList) {
            int result = tblDutyMapper.createDuty(number, area, dutyTime, arrangerId, tblDutyGroup.getMember_id(), clubId, isMixed);
            tblDutyCirculationMapper.setCirculationByClubId(clubId, 0);
            if (result == 0) {
                throw new ServiceException(ServiceStatus.CONFLICT, "创建失败");
            }
        }
    }

    @Override
    public void deleteDutyAllByGroup(LocalDateTime dutyTime, String groupName, Long club_id) {
        List<TblDutyGroup> dutyGroupList = tblDutyGroupMapper.selectUserIdByGroupName(club_id, groupName);
        for (TblDutyGroup tblDutyGroup : dutyGroupList) {
            if (tblDutyMapper.deleteDuty(dutyTime, tblDutyGroup.getMember_id(), club_id) != 1) {
                throw new ServiceException(ServiceStatus.CONFLICT, "删除失败");
            }
        }
    }

    @Override
    @Transactional
    public void deleteDutyByUser(LocalDateTime dutyTime, String cleaner_id, Long club_id) {
        if (tblDutyMapper.deleteDuty(dutyTime, cleaner_id, club_id) != 1) {
            throw new ServiceException(ServiceStatus.CONFLICT, "删除失败");
        }
    }

    @Override
    @Transactional
    public void uploadDutyPicture(LocalDateTime dutyTime, String memberId, Long clubId, List<MultipartFile> filelist) {
        List<String> fileIdList = new ArrayList<>();
        for (MultipartFile file : filelist) {

            String uploadFile = "/duty/" + memberId + "/" + dutyTime.toString();
            String fileName = memberId + dutyTime;

            UploadFileQO uploadFileQO = new UploadFileQO();
            uploadFileQO.setOverwrite(true);
            uploadFileQO.setTargetFilename(fileName);
            uploadFileQO.setTargetFolder(uploadFile);

            String fileId = FileStorageService.uploadFile(file, CLOUD, uploadFileQO);

            fileIdList.add(fileId);
        }
        StringBuilder files = new StringBuilder();
        for (String fileId : fileIdList) {
            files.append(fileId).append(",");
        }

        if (1 != tblDutyMapper.setDutyPicture(dutyTime, memberId, clubId, String.valueOf(files))) {
            log.error("上传值日结果反馈失败，反馈的图片已上传成功，但将fileId 保存到数据库失败");
            for (String fileId : fileIdList) {
                FileStorageService.deleteFile(fileId);
            }
            throw new ServiceException(ServiceStatus.CONFLICT, "上传失败");
        }
    }

    @Override
    public PageVO<DutyInfoVO> selectDuty(DutyInfoQO qo) {

        Page<TblDuty> page = tblDutyMapper.selectDuty(
                new Page<>(qo.getPageNum(), qo.getSize()), qo.getClub_id()
        );
        List<DutyInfoVO> dutyList = new ArrayList<>();
        SelectUserName(dutyList, page);
        if (page.getTotal() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "值日信息");
        } else {
            return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize()));
        }
    }

    @Override
    public PageVO<DutyInfoVO> selectDutyByNumber(DutyInfoQO qo) {
        Page<TblDuty> page = tblDutyMapper.selectDutyByNumber(
                new Page<>(qo.getPageNum(), qo.getSize()), qo.getClub_id(), qo.getNumber()
        );
        List<DutyInfoVO> dutyList = new ArrayList<>();
        SelectUserName(dutyList, page);
        if (page.getTotal() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "值日信息");
        } else {
            return new PageVO<>(dutyList,new Page<>(qo.getPageNum(), qo.getSize()));
        }
    }

    @Override
    public PageVO<DutyInfoVO> selectDutyByName(DutyInfoQO qo) {
        List<UserBasicInfoDTO> nameList = UserInfoService.searchUser(qo.getName());
        if (nameList.size() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "查无此人");
        }
        List<DutyInfoVO> dutyList = new ArrayList<>();
        for (UserBasicInfoDTO userBasicInfoDTO : nameList) {
            Page<TblDuty> page = tblDutyMapper.selectDutyByName(
                    new Page<>(qo.getPageNum(), qo.getSize()), qo.getClub_id(), userBasicInfoDTO.getUserId()
            );
            SelectUserName(dutyList, page);
        }
        if (dutyList.size() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "查无此人");
        }
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize()));
    }

    @Override
    public PageVO<DutyInfoVO> selectDutyByNumberAndName(DutyInfoQO qo) {
        List<UserBasicInfoDTO> nameList = UserInfoService.searchUser(qo.getName());
        if (nameList.size() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "查无此人");
        }
        List<DutyInfoVO> dutyList = new ArrayList<>();
        for (UserBasicInfoDTO userBasicInfoDTO : nameList) {
            Page<TblDuty> page = tblDutyMapper.selectDutyByNumberAndName(
                    new Page<>(qo.getPageNum(), qo.getSize()), qo.getClub_id(), userBasicInfoDTO.getUserId(), qo.getNumber()
            );
            SelectUserName(dutyList, page);
        }
        if (dutyList.size() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "查无此人");
        }
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize()));
    }

    private void SelectUserName(List<DutyInfoVO> dutyList, Page<TblDuty> page) {
        for(TblDuty tblDuty : page.getRecords()){
            DutyInfoVO dutyInfoVO = new DutyInfoVO();
            dutyInfoVO.setId(tblDuty.getId());
            dutyInfoVO.setNumber(tblDuty.getNumber());
            dutyInfoVO.setArea(tblDuty.getArea());
            dutyInfoVO.setDuty_time(tblDuty.getDuty_time());
            dutyInfoVO.setCleaner_id(tblDuty.getCleaner_id());
            dutyInfoVO.setCleaner_name(UserInfoService.getUserName(tblDuty.getCleaner_id()));
            dutyInfoVO.setArranger_id(tblDuty.getArranger_id());
            dutyInfoVO.setArranger_name(UserInfoService.getUserName(tblDuty.getArranger_id()));
            dutyInfoVO.setImage_file(tblDuty.getImage_file());
            dutyInfoVO.setIs_mixed(tblDuty.getIs_mixed());
            dutyList.add(dutyInfoVO);
        }
    }
}
