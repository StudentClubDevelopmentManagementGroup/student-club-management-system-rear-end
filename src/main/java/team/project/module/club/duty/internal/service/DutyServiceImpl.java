package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.common.util.StringUtils;
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
import team.project.module.club.duty.internal.model.query.DutyInfoSelfQO;
import team.project.module.club.duty.internal.model.view.DutyInfoVO;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static team.project.module.util.filestorage.export.model.enums.FileStorageType.CLOUD;
import static team.project.module.util.filestorage.export.model.enums.FileStorageType.LOCAL;

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
    FileStorageServiceI fileStorageServiceI;

    @Autowired
    UserInfoServiceI userInfoServiceI;

    @Override
    public void createDuty(String number, String area, LocalDateTime dateTime, String arrangerId, String cleanerId, Long clubId, Boolean isMixed) {
        TblDuty tblDuty = tblDutyMapper.selectOne(number, area, dateTime, arrangerId, cleanerId, clubId);
        if (tblDuty != null) {
            throw new ServiceException(ServiceStatus.CONFLICT, "重复创建");
        }
        if (tblDutyMapper.createDuty(number, area, dateTime, arrangerId, cleanerId, clubId, isMixed) != 1) {
            throw new ServiceException(ServiceStatus.CONFLICT, "创建失败");
        }
    }

    @Override
    @Transactional
    public void createDutyByGroup(String number, String area, LocalDateTime dutyTime, String arrangerId, Long clubId, Boolean isMixed, String groupName) {
        List<TblDutyGroup> dutyGroupList = tblDutyGroupMapper.selectUserIdByGroupName(clubId, groupName);
        if (dutyGroupList == null || dutyGroupList.isEmpty()) {
            throw new ServiceException(ServiceStatus.CONFLICT, "查询到的值为空或小组成员列表不存在");
        }
        for (TblDutyGroup tblDutyGroup : dutyGroupList) {
            TblDuty tblDuty = tblDutyMapper.selectOne(number, area, dutyTime, arrangerId, tblDutyGroup.getMemberId(), clubId);
            if (tblDuty != null) {
                throw new ServiceException(ServiceStatus.CONFLICT, "重复创建");
            }
            int result = tblDutyMapper.createDuty(number, area, dutyTime, arrangerId, tblDutyGroup.getMemberId(), clubId, isMixed);
            tblDutyCirculationMapper.setCirculationByClubId(clubId, 0);
            if (result == 0) {
                throw new ServiceException(ServiceStatus.CONFLICT, "创建失败");
            }
        }
    }

    @Override
    public void deleteDutyAllByGroup(LocalDateTime dutyTime, String groupName, Long clubId) {
        List<TblDutyGroup> dutyGroupList = tblDutyGroupMapper.selectUserIdByGroupName(clubId, groupName);
        if (dutyGroupList == null || dutyGroupList.isEmpty()) {
            throw new ServiceException(ServiceStatus.CONFLICT, "查询到的值为空或小组成员列表不存在");
        }
        for (TblDutyGroup tblDutyGroup : dutyGroupList) {
            if (tblDutyMapper.deleteDuty(dutyTime, tblDutyGroup.getMemberId(), clubId) != 1) {
                throw new ServiceException(ServiceStatus.CONFLICT, "删除失败");
            }
        }
    }

    @Override
    @Transactional
    public void deleteDutyByUser(LocalDateTime dutyTime, String cleanerId, Long clubId) {
        if (tblDutyMapper.deleteDuty(dutyTime, cleanerId, clubId) != 1) {
            throw new ServiceException(ServiceStatus.CONFLICT, "删除失败");
        }
    }

    @Override
    @Transactional
    public List<String> uploadDutyPictures(LocalDateTime dutyTime, String memberId, Long clubId, MultipartFile[] files) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String uploadFileBasePath = "/duty/" + memberId + "/" + dutyTime.format(fmt);

        List<String> fileIds = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String fileType = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String fileName = memberId + "_" + System.currentTimeMillis() + fileType; // 使用时间戳避免文件名重复

                UploadFileQO uploadFileQO = new UploadFileQO();
                uploadFileQO.setOverwrite(true);
                uploadFileQO.setTargetFilename(fileName);
                uploadFileQO.setTargetFolder(uploadFileBasePath);

                try {
                    String fileId = fileStorageServiceI.uploadFile(file, CLOUD, uploadFileQO);
                    fileIds.add(fileId);
                } catch (FileStorageException e) {
                    fileIds.forEach(fileStorageServiceI::deleteFile);
                    throw new ServiceException(ServiceStatus.CONFLICT, "上传失败");
                }
            } else {
                log.warn("跳过空文件");
            }
        }

        // 将所有文件ID以逗号分隔后存储
        String allFileIds = String.join(",", fileIds);
        if (1 != tblDutyMapper.setDutyPicture(dutyTime, memberId, clubId, allFileIds)) {
            fileIds.forEach(fileStorageServiceI::deleteFile);
            throw new ServiceException(ServiceStatus.CONFLICT, "上传失败");
        }
        else {
            List<String> fileUrlList = new ArrayList<>();
            for (String fileId : fileIds) {
                // 确保fileId不为空或空白后调用服务方法
                if (StringUtils.isNotBlank(fileId)) {
                    String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                    // 使用获取到的fileUrl进行后续操作，比如打印、保存或进一步处理
                    fileUrlList.add(fileUrl);
                }
            }
            return fileUrlList;
        }
    }

    @Override
    public PageVO<DutyInfoVO> selectDuty(DutyInfoQO qo) {

        Page<TblDuty> page = tblDutyMapper.selectDuty(
                new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId()
        );
        List<DutyInfoVO> dutyList = new ArrayList<>();
        selectUserName(dutyList, page);
        if (page.getTotal() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "值日信息");
        } else {
            return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize(), page.getTotal()));
        }
    }

    @Override
    public PageVO<DutyInfoVO> selectDutyByNumber(DutyInfoQO qo) {
        Page<TblDuty> page = tblDutyMapper.selectDutyByNumber(
                new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId(), qo.getNumber()
        );
        List<DutyInfoVO> dutyList = new ArrayList<>();
        selectUserName(dutyList, page);
        if (page.getTotal() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "值日信息为空");
        } else {
            return new PageVO<>(dutyList,new Page<>(qo.getPageNum(), qo.getSize(), page.getTotal()));
        }
    }

    @Override
    public PageVO<DutyInfoVO> selectDutyByName(DutyInfoQO qo) {
        List<UserBasicInfoDTO> nameList = userInfoServiceI.searchUser(qo.getName());
        if (nameList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        List<DutyInfoVO> dutyList = new ArrayList<>();
        for (UserBasicInfoDTO userBasicInfoDTO : nameList) {
            Page<TblDuty> page = tblDutyMapper.selectDutyByName(
                    new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId(), userBasicInfoDTO.getUserId()
            );
            selectUserName(dutyList, page);
        }
        if (dutyList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize(), nameList.size()));
    }

    @Override
    public PageVO<DutyInfoVO> selectDutyByNumberAndName(DutyInfoQO qo) {
        List<UserBasicInfoDTO> nameList = userInfoServiceI.searchUser(qo.getName());
        if (nameList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        List<DutyInfoVO> dutyList = new ArrayList<>();
        for (UserBasicInfoDTO userBasicInfoDTO : nameList) {
            Page<TblDuty> page = tblDutyMapper.selectDutyByNumberAndName(
                    new Page<>(qo.getPageNum(), qo.getSize()), qo.getClubId(), userBasicInfoDTO.getUserId(), qo.getNumber()
            );
            selectUserName(dutyList, page);
        }
        if (dutyList.size() == 0) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "查无此人");
        }
        return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize(), nameList.size()));
    }

    @Override
    public PageVO<DutyInfoVO> selectDutyByUserId(DutyInfoSelfQO qo, String userId) {
        Page<TblDuty> page = tblDutyMapper.selectDutyByUserId(
                new Page<>(qo.getPageNum(), qo.getSize()), userId
        );
        List<DutyInfoVO> dutyList = new ArrayList<>();
        selectUserName(dutyList, page);
        if (page.getTotal() == 0) {
            throw new ServiceException(ServiceStatus.SUCCESS, "值日信息");
        } else {
            return new PageVO<>(dutyList, new Page<>(qo.getPageNum(), qo.getSize(), page.getTotal()));
        }
    }

    private void selectUserName(List<DutyInfoVO> dutyList, Page<TblDuty> page) {
        for(TblDuty tblDuty : page.getRecords()){
            DutyInfoVO dutyInfoVO = new DutyInfoVO();
            dutyInfoVO.setId(tblDuty.getId());
            dutyInfoVO.setClubId(tblDuty.getClubId());
            dutyInfoVO.setUpdateTime(tblDuty.getUpdateTime());
            dutyInfoVO.setDeleted(tblDuty.getDeleted());
            dutyInfoVO.setNumber(tblDuty.getNumber());
            dutyInfoVO.setArea(tblDuty.getArea());
            dutyInfoVO.setDateTime(tblDuty.getDutyTime());
            dutyInfoVO.setCleanerId(tblDuty.getCleanerId());
            dutyInfoVO.setCleanerName(userInfoServiceI.getUserName(tblDuty.getCleanerId()));
            dutyInfoVO.setArrangerId(tblDuty.getArrangerId());
            dutyInfoVO.setArrangerName(userInfoServiceI.getUserName(tblDuty.getArrangerId()));
            if(tblDuty.getImageFile() == null)
            {
                dutyInfoVO.setImageFile(null);
            }
            else{
                String[] fileIdArray = tblDuty.getImageFile().split(",");
                List<String> fileUrlList = new ArrayList<>();
                for (String fileId : fileIdArray) {
                        String fileUrl = fileStorageServiceI.getFileUrl(fileId.trim());
                        // 使用获取到的fileUrl进行后续操作，比如打印、保存或进一步处理
                        fileUrlList.add(fileUrl);
                }
                dutyInfoVO.setImageFile(fileUrlList);
            }
            dutyInfoVO.setIsMixed(tblDuty.getIsMixed());
            dutyList.add(dutyInfoVO);
        }
    }
}
