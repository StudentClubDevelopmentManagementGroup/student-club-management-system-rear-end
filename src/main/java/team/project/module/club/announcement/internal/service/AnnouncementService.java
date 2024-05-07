package team.project.module.club.announcement.internal.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.announcement.internal.mapper.AnnouncementMapper;
import team.project.module.club.announcement.internal.model.entity.AnnouncementDO;
import team.project.module.club.announcement.internal.model.request.PublishAnnouncementReq;
import team.project.module.club.announcement.internal.model.view.AnnouncementVO;
import team.project.module.club.announcement.internal.util.ModelConverter;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.export.service.FileStorageServiceI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AnnouncementService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageServiceI fileStorageService;

    @Autowired
    AnnouncementMapper announcementMapper;

    @Autowired
    ModelConverter modelConverter;

    public Long upload(String authorId, PublishAnnouncementReq req) {

        /* 先将公告的内容保存到文件，获取 fileId 后，再将 fileId 和其他信息保存到数据库 */

        String targetFolder = "/club/announcement/announcement/" + req.getClubId();
        String randomFileName = UUID.randomUUID().toString().replace("-", "") + ".txt";

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(targetFolder);
        uploadFileQO.setTargetFilename(randomFileName);
        uploadFileQO.setOverwrite(false);

        String textFileId = fileStorageService.uploadTextToFile(FileStorageType.LOCAL, req.getContent(), uploadFileQO);

        AnnouncementDO announcement = new AnnouncementDO();
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

    public AnnouncementVO read(Long announcementId) {

        /* ljh_TODO: 设置公告的可见性
            查询数据库获取公告的基本信息，如何判断公告对该用户是否可见，之后从文件中读出公告内容一并返回 */

        /* tmp */
        AnnouncementDO announcementDO = announcementMapper.selectOne(new LambdaQueryWrapper<AnnouncementDO>()
            .select(
                AnnouncementDO::getTitle,
                AnnouncementDO::getTextFile
            )
            .eq(AnnouncementDO::getId, announcementId)
        );

        String fileId = announcementDO.getTextFile();
        String content = fileStorageService.getTextFromFile(fileId);

        return modelConverter.toAnnouncementVO(announcementDO, content);
    }

    public List<AnnouncementVO> list() {

        /* tmp */
        List<AnnouncementDO> announcementDOList = announcementMapper.selectList(new LambdaQueryWrapper<AnnouncementDO>()
            .orderBy(true, true, AnnouncementDO::getCreateTime)
        );

        List<AnnouncementVO> result = new ArrayList<>();
        for (AnnouncementDO announcementDO : announcementDOList) {
            result.add( modelConverter.toAnnouncementVO(announcementDO, null) ); /* <- 不返回 content */
        }

        return result;
    }
}
