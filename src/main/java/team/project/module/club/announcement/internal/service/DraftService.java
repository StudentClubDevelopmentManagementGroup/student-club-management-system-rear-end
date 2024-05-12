package team.project.module.club.announcement.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.announcement.internal.mapper.DraftMapper;
import team.project.module.club.announcement.internal.model.entity.DraftDO;
import team.project.module.club.announcement.internal.model.request.AnnDetail;
import team.project.module.club.announcement.internal.model.request.DraftSaveReq;
import team.project.module.club.announcement.internal.model.view.DraftVO;
import team.project.module.club.announcement.internal.util.ModelConverter;
import team.project.module.util.filestorage.export.model.enums.FileStorageType;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;
import team.project.module.util.filestorage.export.util.FileStorageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DraftService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AuthServiceI authService;

    @Autowired
    FileStorageServiceI fileStorageService;

    @Autowired
    DraftMapper draftMapper;

    @Autowired
    ModelConverter modelConverter;

    private static final String DRAFT_FOLDER = "/club/announcement/draft/";

    private static final FileStorageType STORAGE_TYPE = FileStorageType.CLOUD;

    /**
     * 创建一篇新的草稿
     * */
    public void createDraft(AnnDetail draft) {
        authService.requireClubManager(draft.getAuthorId(), draft.getClubId(), "只有社团负责人能编辑公告");

        /* 将草稿的内容保存到文件，获取 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(DRAFT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".html"));
        uploadFileQO.setOverwrite(false);

        String textFileId = fileStorageService.uploadTextToFile(STORAGE_TYPE, draft.getContent(), uploadFileQO);

        /* 将 fileId 和其他信息保存到数据库 */

        DraftDO draftDO = new DraftDO();
        draftDO.setAuthorId(draft.getAuthorId());
        draftDO.setClubId(draft.getClubId());
        draftDO.setTitle(draft.getTitle());
        draftDO.setSummary(draft.getSummary());
        draftDO.setTextFile(textFileId);

        try {
            draftMapper.insert(draftDO);
        }
        catch (Exception e) {
            fileStorageService.deleteFile(textFileId); /* <- 保存草稿失败，删除文件 */

            if (e instanceof DataIntegrityViolationException) {
                log.info("保存草稿失败：（可能是因为外键社团id不存在？）", e);
                throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "保存失败");
            }
            else {
                log.error("保存草稿失败", e);
                throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "保存失败");
            }
        }

        /* return draft.getDraftId(); */
    }

    /**
     * 更新草稿（其实是删除旧草稿，创建新草稿）
     * */
    public void updateDraft(DraftSaveReq req) {
        AnnDetail draft = req.getDraft();

        /* 查询数据库获取旧草稿，并校验权限 */

        authService.requireClubManager(draft.getAuthorId(), draft.getClubId(), "只有社团负责人能编辑公告");

        DraftDO oldDraftDO = draftMapper.selectDraftBasicInfo(req.getDraftId());

        if (oldDraftDO == null) {
            throw new ServiceException(ServiceStatus.UNPROCESSABLE_ENTITY, "找不到草稿");
        } else if ( ! Objects.equals( oldDraftDO.getAuthorId(), draft.getAuthorId() )) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该草稿作者，无权修改");
        } else if ( ! Objects.equals( oldDraftDO.getClubId(), draft.getClubId() )) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "呃"); /* <- 正常业务流不会触发该异常 */
        }

        /* 将新草稿的内容保存到新文件，获取新文件的 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(DRAFT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".html"));
        uploadFileQO.setOverwrite(false);

        String newTextFileId = fileStorageService.uploadTextToFile(STORAGE_TYPE, draft.getContent(), uploadFileQO);

        /* 更新数据库 */

        DraftDO newDraftDO = new DraftDO();
        newDraftDO.setDraftId(req.getDraftId());
        newDraftDO.setTitle(draft.getTitle());
        newDraftDO.setSummary(draft.getSummary());
        newDraftDO.setTextFile(newTextFileId);

        try {
            draftMapper.updateById(newDraftDO);
        }
        catch (Exception e) {
            fileStorageService.deleteFile(newTextFileId); /* <- 如果数据库更新失败，则删除新文件 */
            log.error("保存草稿失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "保存草稿失败");
        }

        /* 数据库更新成功后，删除旧文件 */

        String oldTextFileId = oldDraftDO.getTextFile();
        fileStorageService.deleteFile(oldTextFileId);
    }

    /**
     * 获取某篇草稿的内容
     * */
    public DraftVO readDraft(String authorId, Long draftId) {

        DraftDO draft = draftMapper.selectById(draftId);

        if (draft == null) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "找不到草稿");
        } else if ( ! Objects.equals( draft.getAuthorId(), authorId )) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该草稿作者，无权修改");
        }

        String fileId = draft.getTextFile();
        String content = fileStorageService.getTextFromFile(fileId);
        if (content == null) {
            log.error("读取草稿失败（数据库中存有记录，但是依据 fileId 找不到指定文件）");
            throw new ServiceException(ServiceStatus.NOT_FOUND, "读取草稿失败");
        }

        return modelConverter.toDraftVO(draft, content, null);
    }

    /**
     * 查看我的草稿箱
     * */
    public PageVO<DraftVO> listMyDraft(PagingQueryReq req, String authorId, Long clubId) {
        authService.requireClubManager(authorId, clubId, "只有社团负责人能编辑公告");

        Page<DraftDO> page = new Page<>(req.getPageNum(), req.getPageSize(), true);
        List<DraftDO> draftList = draftMapper.listMyDraft(page, authorId, clubId);

        List<DraftVO> result = new ArrayList<>();
        for (DraftDO draftDO : draftList) {
            result.add( modelConverter.toDraftVO(draftDO, null, draftDO.getSummary()) );
        }

        return new PageVO<>(result, page);
    }

    /**
     * 删除某篇草稿
     * */
    public void deleteDraft(String authorId, Long draftId) {

        DraftDO draft = draftMapper.selectDraftBasicInfo(draftId);

        if (draft == null) {
            return;
        } else if ( ! Objects.equals( draft.getAuthorId(), authorId )) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "不是该草稿作者，无权删除");
        }

        String textFileId = draft.getTextFile();

        if (1 == draftMapper.deleteById(draft)) { /* <- 删除数据库的数据，删除成功后清除文件 */
            fileStorageService.deleteFile(textFileId);
        }
    }
}
