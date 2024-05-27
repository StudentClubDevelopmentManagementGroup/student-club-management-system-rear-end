package team.project.module.club.announcement.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.mapper.AnnMapper;
import team.project.module.club.announcement.internal.mapper.DraftMapper;
import team.project.module.club.announcement.internal.model.entity.AnnDO;
import team.project.module.club.announcement.internal.model.entity.DraftDO;
import team.project.module.club.announcement.internal.model.query.AnnSearchQO;
import team.project.module.club.announcement.internal.model.request.AnnDetail;
import team.project.module.club.announcement.internal.model.request.AnnPublishReq;
import team.project.module.club.announcement.internal.model.request.AnnSearchReq;
import team.project.module.club.announcement.internal.model.view.AnnDetailVO;
import team.project.module.club.announcement.internal.util.ModelConverter;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.util.filestorage.export.model.enums.FileStorageType;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;
import team.project.module.util.filestorage.export.util.FileStorageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AnnService {

    @Autowired
    AuthServiceI authService;

    @Autowired
    UserInfoServiceI userInfoService;

    @Autowired
    PceIService clubMemberRoleService;

    @Autowired
    FileStorageServiceI fileStorageService;

    @Autowired
    DraftService draftService;

    @Autowired
    DraftMapper draftMapper;

    @Autowired
    AnnMapper announcementMapper;

    @Autowired
    ModelConverter modelConverter;

    private static final String ANNOUNCEMENT_FOLDER = "/club/announcement/announcement";

    private static final FileStorageType STORAGE_TYPE = FileStorageType.CLOUD;

    public void publishAnn(AnnPublishReq req) {

        Long      deleteDraft  = req.getDraftId();
        AnnDetail announcement = req.getAnnouncement();
        String    authorId     = announcement.getAuthorId();
        Long      clubId       = announcement.getClubId();

        /* 校验权限 */

        authService.requireClubManager(authorId, clubId, "只有社团负责人能发布公告");

        DraftDO draftDO = null;
        if (null != deleteDraft) {
            draftDO = draftMapper.selectDraftBasicInfo(deleteDraft);

            if (draftDO == null) {
                throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "找不到草稿");
            } else if ( ! Objects.equals( draftDO.getAuthorId(), authorId )) {
                throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该草稿作者");
            } else if ( ! Objects.equals( draftDO.getClubId(), clubId )) {
                throw new ServiceException(ServiceStatus.FORBIDDEN, "呃"); /* <- 正常业务流不会触发该异常 */
            }
        }

        /* 将公告的内容保存到文件，获取文件的 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(ANNOUNCEMENT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".html"));
        uploadFileQO.setOverwrite(false);

        String textFileId = fileStorageService.uploadTextToFile(STORAGE_TYPE, announcement.getContent(), uploadFileQO);

        /* 更新数据库 */

        AnnDO announcementDO = new AnnDO();
     /* announcementDO.setPublishTime(new Timestamp(System.currentTimeMillis())); */
        announcementDO.setAuthorId(authorId);
        announcementDO.setClubId(clubId);
        announcementDO.setTitle(announcement.getTitle());
        announcementDO.setSummary(announcement.getSummary());
        announcementDO.setTextFile(textFileId);

        try {
            announcementMapper.insert(announcementDO);
        }
        catch (Exception e) {
            fileStorageService.deleteFile(textFileId); /* <- 保存公告失败，删除文件 */

            if (e instanceof DataIntegrityViolationException) {
                log.info("发布公告失败：（可能是因为外键社团id不存在？）", e);
                throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "发布失败");
            }
            else {
                log.error("发布公告失败", e);
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "发布失败");
            }
        }

        /* 发布成功后删除草稿 */

        if (null != deleteDraft) {
            if (1 == draftMapper.deleteById(deleteDraft)) {
                fileStorageService.deleteFile(draftDO.getTextFile());
            }
        }
    }

    public AnnDetailVO readAnn(Long announcementId) {

        /* ljh_TODO: 设置公告的可见性
            查询数据库获取公告的基本信息，如何判断公告对该用户是否可见，之后从文件中读出公告内容一并返回 */

        AnnDO announcementDO = announcementMapper.selectById(announcementId);

        String fileId = announcementDO.getTextFile();
        String content = fileStorageService.getTextFromFile(fileId);
        if (content == null) {
            log.error("读取公告失败（数据库中存有记录，但是依据 fileId 无法读取公告内容）");
            throw new ServiceException(ServiceStatus.NOT_FOUND, "读取公告内容失败");
        }

        return modelConverter.toAnnDetailVO(announcementDO, content, null);
    }

    public PageVO<AnnDetailVO> searchAnn(PagingQueryReq pageReq, AnnSearchReq searchReq) {
        Page<AnnDO> page = new Page<>(pageReq.getPageNum(), pageReq.getPageSize(), true);

        String titleKeyword = searchReq.getTitleKeyword();

        List<String> authorIdList = new ArrayList<>();
        if (null != searchReq.getAuthorName()) {
            List<UserBasicInfoDTO> authors = userInfoService.searchUser(searchReq.getAuthorName());
            for (UserBasicInfoDTO author : authors) {
                authorIdList.add(author.getUserId());
            }
        }
        if (null != searchReq.getAuthorId()) {
            authorIdList.add(searchReq.getAuthorId());
        }

        AnnSearchQO searchQO = new AnnSearchQO();
        searchQO.setClubId(searchReq.getClubId());
        searchQO.setAuthorIdList(authorIdList);
        searchQO.setTitleKeyword(titleKeyword == null || titleKeyword.isBlank() ? null : titleKeyword);
        searchQO.setFromDate(searchReq.getFromDate());
        searchQO.setToDate(searchReq.getToDate());
        List<AnnDO> announcementList = announcementMapper.searchAnn(page, searchQO);

        List<AnnDetailVO> result = new ArrayList<>();
        for (AnnDO annDO : announcementList) {
            result.add( modelConverter.toAnnDetailVO(annDO, null, annDO.getSummary()) );
        }

        return new PageVO<>(result, page);
    }

    @Transactional
    public void deleteAnn(String userId, Long announcementId) {

        AnnDO announcement = announcementMapper.selectAnnBasicInfo(announcementId);
        if (null == announcement) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "找不到公告");
        }
        String authorId = announcement.getAuthorId();
        Long   clubId   = announcement.getClubId();

        /* 校验权限 */

        Integer authorRole;
        if (Objects.equals(authorId, userId)) {
            authService.requireClubManager(userId, clubId, "需要社团负责人才能删除公告");
        } else if ( ! clubMemberRoleService.isClubManager(authorId, clubId)) {
            authService.requireClubTeacherManager(userId, clubId, "需要社团的教师负责人才能删除该公告");
        } else if (null == (authorRole = userInfoService.selectUserRole(authorId))) {
            authService.requireSuperAdmin(userId, "需要超级管理员能删除该公告"); /* <- 正常业务流不应到此 */
        } else if (UserRole.hasRole(authorRole, UserRole.TEACHER)) {
            authService.requireSuperAdmin(userId, "需要超级管理员能删除该公告");
        } else if (UserRole.hasRole(authorRole, UserRole.STUDENT)) {
            authService.requireClubTeacherManager(userId, clubId, "需要社团的教师负责人才能删除该公告");
        } else {
            authService.requireSuperAdmin(userId, "需要超级管理员能删除该公告"); /* <- 正常业务流不应到此 */
        }

        /* 删除数据库的记录 */

        if (1 != announcementMapper.deleteById(announcementId)) {
            log.error("删除公告失败");
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "删除失败");
        }

        /* 不删除文件，因为数据库的记录是逻辑删除，而不是真的删除
            String textFileId = announcement.getTextFile(); <- 此处 textFileId 可能为 null，可能 SQL 没查
            fileStorageService.deleteFile(textFileId);
        */
    }

    public void toDraft(String userId, Long announcementId) {

        AnnDO announcement = announcementMapper.selectById(announcementId);
        if (null == announcement) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "找不到公告");
        }

        authService.requireClubManager(userId, announcement.getClubId(), "需要社团负责人才能编辑公告");

        if ( ! Objects.equals(userId, announcement.getAuthorId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是公告作者");
        }

        String content = fileStorageService.getTextFromFile(announcement.getTextFile());
        if (content == null) {
            log.error("将公告转为草稿失败（数据库中存有记录，但是依据 fileId 找不到指定文件）");
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "找不到公告");
        }

        AnnDetail draft = new AnnDetail();
        draft.setClubId(announcement.getClubId());
        draft.setAuthorId(announcement.getAuthorId());
        draft.setTitle(announcement.getTitle());
        draft.setContent(content);
        draft.setSummary(announcement.getSummary());

        draftService.createDraft(draft);
    }
}
