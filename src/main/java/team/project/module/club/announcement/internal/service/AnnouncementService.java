package team.project.module.club.announcement.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.announcement.internal.mapper.AnnouncementMapper;
import team.project.module.club.announcement.internal.model.entity.AnnouncementDO;
import team.project.module.club.announcement.internal.model.request.PublishAnnouncementReq;
import team.project.module.club.announcement.internal.model.view.AnnouncementDetailVO;
import team.project.module.club.announcement.internal.util.ModelConverter;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.export.service.FileStorageServiceI;
import team.project.module.filestorage.export.util.FileStorageUtil;

import java.util.List;

@Service
public class AnnouncementService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageServiceI fileStorageService;

    @Autowired
    AnnouncementMapper announcementMapper;

    @Autowired
    ModelConverter modelConverter;

    private static final String DRAFT_FOLDER = "/club/announcement/announcement/";

    private static final FileStorageType STORAGE_TYPE = FileStorageType.LOCAL; /* <- tmp */

    public void publishAnnouncement(String authorId, PublishAnnouncementReq req) {

        /* 将公告的内容保存到文件，获取 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(DRAFT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".html"));
        uploadFileQO.setOverwrite(false);

        String textFileId = fileStorageService.uploadTextToFile(STORAGE_TYPE, req.getContent(), uploadFileQO);

        /* 将 fileId 和其他信息保存到数据库 */

        AnnouncementDO announcement = new AnnouncementDO();
        announcement.setAuthorId(authorId);
        announcement.setClubId(req.getClubId());
        announcement.setTitle(req.getTitle());
        announcement.setSummary(req.getSummary());
        announcement.setTextFile(textFileId);

        try {
            announcementMapper.insert(announcement);
        } catch (Exception e) {
            fileStorageService.deleteFile(textFileId); /* <- 保存公告失败，删除文件 */

            if (e instanceof DataIntegrityViolationException) {
                log.info("发布公告失败：（可能是因为外键社团id不存在？）", e);
                throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "发布失败");
            } else {
                log.error("发布公告失败", e);
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "发布失败");
            }
        }

        /* return announcement.getAnnouncementId(); */
    }

    public AnnouncementDetailVO readAnnouncement(Long announcementId) {

        /* ljh_TODO: 设置公告的可见性
            查询数据库获取公告的基本信息，如何判断公告对该用户是否可见，之后从文件中读出公告内容一并返回 */

        AnnouncementDO announcementDO = announcementMapper.selectById(announcementId);

        String fileId = announcementDO.getTextFile();
        String content = fileStorageService.getTextFromFile(fileId);
        if (content == null) {
            log.error("读取公告失败（数据库中存有记录，但是依据 fileId 找不到指定文件）");
            throw new ServiceException(ServiceStatus.NOT_FOUND, "读取公告内容失败");
        }

        return modelConverter.toAnnouncementDetailVO(announcementDO, content);
    }

    public List<AnnouncementDetailVO> list() {
        return null;
    }
}
