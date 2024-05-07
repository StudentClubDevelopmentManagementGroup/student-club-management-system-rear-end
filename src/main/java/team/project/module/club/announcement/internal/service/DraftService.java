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

    public void createDraft(String authorId, SaveDraftReq req) {

        /* 将草稿的内容保存到文件，获取 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(DRAFT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".txt"));
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
            fileStorageService.deleteFile(textFileId);
            log.error("保存草稿失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "保存草稿失败");
        }

        /* return draft.getDraftId(); */
    }

    public void updateDraft(String authorId, SaveDraftReq req) {

        /* 查询数据库获取，获取旧草稿，获取旧草稿文件的 fileId */

        DraftDO oldDraft = draftMapper.selectOne(new LambdaQueryWrapper<DraftDO>()
            .eq(DraftDO::getDraftId, req.getDraftId())
        );

        if (   oldDraft == null                               /* <- 确保该草稿存在 */
          || ! oldDraft.getAuthorId().equals(authorId)        /* <- 确保这篇草稿属于该作者 */
          || ! oldDraft.getClubId()  .equals(req.getClubId()) /* <- 确保这篇草稿属于该社团 */
        ) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, "无权修改");
        }

        String oldTextFileId = oldDraft.getTextFile();

        /* 将新草稿的内容保存到新文件，获取新文件的 fileId */

        UploadFileQO uploadFileQO = new UploadFileQO();
        uploadFileQO.setTargetFolder(DRAFT_FOLDER);
        uploadFileQO.setTargetFilename(FileStorageUtil.randomFilename(".txt"));
        uploadFileQO.setOverwrite(false);

        String newTextFileId = fileStorageService.uploadTextToFile(FileStorageType.LOCAL, req.getContent(), uploadFileQO);

        /* 更新数据库 */

        try {
            draftMapper.update(new LambdaUpdateWrapper<DraftDO>() /* tmp */
                .eq(DraftDO::getDraftId, req.getDraftId())
                .set(DraftDO::getTitle, req.getTitle())
                .set(DraftDO::getTextFile, newTextFileId)
            );
        }
        catch (Exception e) {
            fileStorageService.deleteFile(newTextFileId); /* <- 如果数据库更新失败，则删除新文件 */
            log.error("保存草稿失败", e);
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "保存草稿失败");
        }

        /* 数据库更新成功后，删除旧文件 */

        fileStorageService.deleteFile(oldTextFileId);
    }

    public DraftVO readDraft(Long draftId) {
        /* tmp */
        DraftDO draftDO = draftMapper.selectOne(new LambdaQueryWrapper<DraftDO>()
            .eq(DraftDO::getDraftId, draftId)
        );

        if (draftDO == null) {
            return null;
        }

        String fileId = draftDO.getTextFile();
        String content = fileStorageService.getTextFromFile(fileId);

        return modelConverter.toDraftVO(draftDO, content);
    }

    public List<DraftVO> list(String authorId, Long clubId) {
        List<DraftDO> draftDOList = draftMapper.selectList(new LambdaQueryWrapper<DraftDO>()
            .eq(DraftDO::getClubId, clubId)
            .eq(DraftDO::getAuthorId, authorId)
            .orderBy(true, true, DraftDO::getCreateTime)
        );

        List<DraftVO> result = new ArrayList<>();
        for (DraftDO draftDO : draftDOList) {
            result.add( modelConverter.toDraftVO(draftDO, null) ); /* <- 不返回 content */
        }

        return result;
    }
}
