package team.project.module.club.announcement.internal.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.announcement.internal.mapper.TblClubAnnouncementMapper;
import team.project.module.club.announcement.internal.model.entity.TblClubAnnouncementDO;
import team.project.module.club.announcement.internal.model.request.UploadAnnouncementReq;
import team.project.module.club.announcement.internal.model.view.AnnouncementVO;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.export.service.FileStorageServiceI;

import java.util.UUID;

@Service
public class AnnouncementService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageServiceI fileStorageService;

    @Autowired
    TblClubAnnouncementMapper announcementMapper;

    public Long upload(String authorId, UploadAnnouncementReq req) {

        String targetFolder = "/club/announcement/" + req.getClubId();
        String randomFileName = UUID.randomUUID().toString().replace("-", "") + ".txt";

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(targetFolder);
        uploadFileQO.setTargetFilename(randomFileName);
        uploadFileQO.setOverwrite(false);

        String textFileId = fileStorageService.uploadTextToFile(FileStorageType.LOCAL, req.getContent(), uploadFileQO);

        TblClubAnnouncementDO announcement = new TblClubAnnouncementDO();
        announcement.setAuthorId(authorId);
        announcement.setClubId(req.getClubId());
        announcement.setTitle(req.getTitle());
        announcement.setTextFile(textFileId);

        try {
            announcementMapper.insert(announcement);
        }
        catch (Exception e) {
            fileStorageService.deleteFile(textFileId);
            log.error("上传公告失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "上传公告失败");
        }

        return announcement.getId();
    }

    public AnnouncementVO read(String userId, Long announcementId) {

        /* tmp */
        TblClubAnnouncementDO announcementDO = announcementMapper.selectOne(new LambdaQueryWrapper<TblClubAnnouncementDO>()
            .select(
                TblClubAnnouncementDO::getTitle,
                TblClubAnnouncementDO::getTextFile
            )
            .eq(TblClubAnnouncementDO::getId, announcementId)
        );

        String fileId = announcementDO.getTextFile();
        String content = fileStorageService.getTextFromFile(fileId);

        AnnouncementVO result = new AnnouncementVO();
        result.setTitle(announcementDO.getTitle());
        result.setContent(content);

        return result;
    }
}
