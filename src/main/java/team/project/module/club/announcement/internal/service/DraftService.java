package team.project.module.club.announcement.internal.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.announcement.internal.mapper.DraftMapper;
import team.project.module.club.announcement.internal.model.entity.DraftDO;
import team.project.module.club.announcement.internal.model.request.SaveDraftReq;
import team.project.module.club.announcement.internal.model.view.DraftVO;
import team.project.module.club.announcement.internal.util.ModelConverter;
import team.project.module.filestorage.export.model.enums.FileStorageType;
import team.project.module.filestorage.export.model.query.UploadFileQO;
import team.project.module.filestorage.export.service.FileStorageServiceI;
import team.project.module.filestorage.export.util.FileStorageUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class DraftService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageServiceI fileStorageService;

    @Autowired
    DraftMapper draftMapper;

    @Autowired
    ModelConverter modelConverter;

    private static final String DRAFT_FOLDER = "/club/announcement/draft/";

    private void checkAuthor(String authorId, DraftDO draft, String message) {
        if (draft == null || ! authorId.equals(draft.getAuthorId())) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, message);
        }
    }

    public void createDraft(String authorId, SaveDraftReq req) {

        /* 将草稿的内容保存到文件，获取 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(DRAFT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".html"));
        uploadFileQO.setOverwrite(false);

        String textFileId = fileStorageService.uploadTextToFile(FileStorageType.LOCAL, req.getContent(), uploadFileQO);

        /* 将 fileId 和其他信息保存到数据库 */

        DraftDO draft = new DraftDO();
        draft.setAuthorId(authorId);
        draft.setClubId(req.getClubId());
        draft.setTitle(req.getTitle());
        draft.setTextFile(textFileId);

        try {
            draftMapper.insert(draft);
        }
        catch (Exception e) {
            fileStorageService.deleteFile(textFileId); /* <- 保存草稿失败，则删除文件 */
            log.error("保存草稿失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "保存草稿失败");
        }

        /* return draft.getDraftId(); */
    }

    public void updateDraft(String authorId, SaveDraftReq req) {

        /* 查询数据库获取旧草稿，获取旧草稿文件的 fileId */

        DraftDO oldDraft = draftMapper.selectById(req.getDraftId());

        checkAuthor(authorId, oldDraft, "不是该草稿作者，无权修改");

        String oldTextFileId = oldDraft.getTextFile();

        /* 将新草稿的内容保存到新文件，获取新文件的 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(DRAFT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".html"));
        uploadFileQO.setOverwrite(false);

        String newTextFileId = fileStorageService.uploadTextToFile(FileStorageType.LOCAL, req.getContent(), uploadFileQO);

        /* 更新数据库 */

        DraftDO newDraft = new DraftDO();
        newDraft.setDraftId(req.getDraftId());
        newDraft.setTitle(req.getTitle());
        newDraft.setTextFile(newTextFileId);

        try {
            draftMapper.updateById(newDraft);
        }
        catch (Exception e) {
            fileStorageService.deleteFile(newTextFileId); /* <- 如果数据库更新失败，则删除新文件 */
            log.error("保存草稿失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "保存草稿失败");
        }

        /* 数据库更新成功后，删除旧文件 */

        fileStorageService.deleteFile(oldTextFileId);
    }

    public DraftVO readDraft(String authorId, Long draftId) {

        DraftDO draft = draftMapper.selectById(draftId);

        checkAuthor(authorId, draft, "不是该草稿作者，不能查看内容");

        String fileId = draft.getTextFile();
        String content = fileStorageService.getTextFromFile(fileId);
        if (content == null) {
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "读取草稿失败");
        }

        return modelConverter.toDraftVO(draft, content);
    }

    public List<DraftVO> list(String authorId, Long clubId) {
        List<DraftDO> draftDOList = draftMapper.selectList(new LambdaQueryWrapper<DraftDO>() /* tmp */
            .eq(DraftDO::getClubId, clubId)
            .eq(DraftDO::getAuthorId, authorId)
            .orderBy(true, true, DraftDO::getCreateTime)
        );

        List<DraftVO> result = new ArrayList<>();
        for (DraftDO draftDO : draftDOList) {
            result.add( modelConverter.toDraftVO(draftDO, null) ); /* <- 列表页不需显示内容，content 传 null */
        }

        return result;
    }

    public void deleteDraft(String authorId, Long draftId) {

        DraftDO draft = draftMapper.selectById(draftId);

        checkAuthor(authorId, draft, "不是该草稿作者，无权删除");

        String textFileId = draft.getTextFile();

        if (1 == draftMapper.deleteById(draftId)) { /* <- 删除数据库的数据，删除成功后清除文件 */
            fileStorageService.deleteFile(textFileId);
        }
    }
}
