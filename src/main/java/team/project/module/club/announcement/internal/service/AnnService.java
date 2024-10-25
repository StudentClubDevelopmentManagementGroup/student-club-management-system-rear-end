package team.project.module.club.announcement.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.config.AnnConfig;
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
import team.project.module.club.announcement.tmp.ClubIdMapper;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;
import team.project.module.util.filestorage.export.util.FileStorageUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AnnService {

    @Autowired private AuthServiceI        authService;
    @Autowired private UserInfoServiceI    userInfoService;
    @Autowired private PceIService         clubMemberRoleService;
    @Autowired private FileStorageServiceI fileStorageService;
    @Autowired private DraftService        draftService;
    @Autowired private DraftMapper         draftMapper;
    @Autowired private AnnMapper           announcementMapper;
    @Autowired private ModelConverter      modelConverter;
    @Autowired private ClubIdMapper        clubIdMapper; /* <- tmp */

    private String loadAnnContent(String fileId) {
        assert fileId != null;

        String content = fileStorageService.getTextFromFile(fileId);
        if (content == null) {
            log.error("读取公告内容失败（数据库中存有记录，但是依据 fileId 无法读取公告内容）");
            throw new ServiceException(ServiceStatus.NOT_FOUND, "读取公告内容失败");
        }
        return content;
    }

    /* --------- */

    public void publishAnn(AnnPublishReq req) {

        Long      deleteDraft = req.getDraftId();
        AnnDetail annDetail   = req.getAnnouncement();
        String    authorId    = annDetail.getAuthorId();
        Long      clubId      = annDetail.getClubId();

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
        uploadFileQO.setTargetFolder(AnnConfig.FOLDER_ANN);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".html"));
        uploadFileQO.setOverwrite(false);

        String textFileId = fileStorageService.uploadTextToFile(AnnConfig.STORAGE_TYPE_ANN, annDetail.getContent(), uploadFileQO);

        /* 更新数据库 */

        AnnDO annDO = new AnnDO();
     /* annDO.setPublishTime(new Timestamp(System.currentTimeMillis())); */
        annDO.setAuthorId(authorId);
        annDO.setClubId(clubId);
        annDO.setTitle(annDetail.getTitle());
        annDO.setSummary(annDetail.getSummary());
        annDO.setTextFile(textFileId);

        try {
            announcementMapper.insert(annDO);
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

    /* --------- */

    @Transactional
    public void deleteAnn(String userId, Long announcementId) {

        AnnDO annDO = announcementMapper.selectAnnBasicInfo(announcementId);
        if (null == annDO) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "找不到公告");
        }
        String authorId = annDO.getAuthorId();
        Long   clubId   = annDO.getClubId();

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
            String textFileId = annDO.getTextFile(); <- 此处 textFileId 可能为 null，可能 SQL 没查
            fileStorageService.deleteFile(textFileId);
        */
    }

    /* --------- */

    public void toDraft(String userId, Long announcementId) {

        AnnDO annDO = announcementMapper.selectById(announcementId);
        if (null == annDO)
            throw new ServiceException(ServiceStatus.NOT_FOUND, "找不到公告");

        authService.requireClubManager(userId, annDO.getClubId(), "需要社团负责人才能编辑公告");

        if ( ! Objects.equals(userId, annDO.getAuthorId()))
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是公告作者");

        String content = fileStorageService.getTextFromFile(annDO.getTextFile());
        if (content == null) {
            log.error("将公告转为草稿失败（数据库中存有记录，但是依据 fileId 找不到指定文件）");
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "找不到公告");
        }

        AnnDetail draft = new AnnDetail();
        draft.setClubId(annDO.getClubId());
        draft.setAuthorId(annDO.getAuthorId());
        draft.setTitle(annDO.getTitle());
        draft.setContent(content);
        draft.setSummary(annDO.getSummary());

        draftService.createDraft(draft);
    }

    /* --------- */

    public AnnDetailVO readAnn(Long announcementId) {

        /* TODO ljh_TODO: 设置公告的可见性
            查询数据库获取公告的基本信息，如何判断公告对该用户是否可见，之后从文件中读出公告内容一并返回 */

        AnnDO annDO = announcementMapper.selectById(announcementId);
        if (null == annDO)
            return null;

        return modelConverter.toAnnDetailVO(annDO, loadAnnContent(annDO.getTextFile()), null);
    }

    public AnnDetailVO getLatestAnn(Long clubId) {
        AnnDO annDO = announcementMapper.selectLatestOne(clubId);
        if (null == annDO)
            return null;

        return modelConverter.toAnnDetailVO(annDO, loadAnnContent(annDO.getTextFile()), null);
    }

    public PageVO<AnnDetailVO> searchAnn(PagingQueryReq pageReq, AnnSearchReq searchReq) {
        Page<AnnDO> page = new Page<>(pageReq.getPageNum(), pageReq.getPageSize(), true);

        HashSet<Long> clubIdColl = new HashSet<>(); /* <- 将社团相关的参数统一转换成 club_id 的集合 */
        if (null != searchReq.getClubId()) {        /* <- 一旦指定 club_id，则忽略 club_name 和 department_id */
            clubIdColl.add(searchReq.getClubId());
        }
        else {
            boolean selectByClub = false; /* <- 标记是否查询指定社团所发的公告 */
            if ( ! StringUtils.isBlank(searchReq.getClubName())) {
                selectByClub = true;
                List<Long> clubIds = clubIdMapper.searchClubByName(searchReq.getClubName());
                clubIdColl.addAll(clubIds); /* <- 将 club_name 转化成 club_id 的集合 */
            }
            if (null != searchReq.getDepartmentId()) {
                selectByClub = true;
                List<Long> clubIds = clubIdMapper.searchClubByDepartmentId(searchReq.getDepartmentId());
                clubIdColl.addAll(clubIds); /* <- 将 department_id 转化成 club_id 的集合 */
            }
            if (selectByClub && clubIdColl.isEmpty())
                return new PageVO<>(List.of(), page); /* <- 要查询指定社团所发的公告，但社团集合为空，则不需要再查询公告 */
        }

        HashSet<String> authorIdColl = new HashSet<>(); /* <- 将作者相关的查询参数统一转化成 author_id 的集合 */
        if (null != searchReq.getAuthorId()) {          /* <- 一旦指定 author_id，则忽略 author_name */
            authorIdColl.add(searchReq.getAuthorId());
        }
        else if ( ! StringUtils.isBlank(searchReq.getAuthorName())) {
            for (UserBasicInfoDTO author : userInfoService.searchUser( searchReq.getAuthorName() ))
                authorIdColl.add(author.getUserId()); /* <- 将 author_name 统一转化成 author_id 的集合 */
            if (authorIdColl.isEmpty())
                return new PageVO<>(List.of(), page);  /* <- 要查询指定作者所发的公告，但作者集合为空，则不需要再查询公告 */
        }

        AnnSearchQO searchQO = new AnnSearchQO();
        searchQO.setClubIdColl(clubIdColl);
        searchQO.setAuthorIdColl(authorIdColl);
        searchQO.setTitleKeyword(StringUtils.trimToNull(searchReq.getTitleKeyword()));
        searchQO.setFromDate(searchReq.getFromDate());
        searchQO.setToDate(searchReq.getToDate());

        List<AnnDetailVO> result = new ArrayList<>();
        for (AnnDO annDO : announcementMapper.searchAnn(page, searchQO)) {
            result.add( modelConverter.toAnnDetailVO(annDO, null, annDO.getSummary()) );
        }

        return new PageVO<>(result, page);
    }
}
