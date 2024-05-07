package team.project.module.club.announcement.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.announcement.internal.mapper.AnnouncementMapper;
import team.project.module.club.announcement.internal.model.request.SaveDraftReq;
import team.project.module.club.announcement.internal.util.ModelConverter;
import team.project.module.filestorage.export.service.FileStorageServiceI;

@Service
public class DraftService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileStorageServiceI fileStorageService;

    @Autowired
    AnnouncementMapper announcementMapper;

    @Autowired
    ModelConverter modelConverter;

    /**
     * 保存草稿，如果 draft_id 为空，则创建新的草稿，否则更新草稿
     * */
    public void save(String authorId, SaveDraftReq req) {
        if (req.getDraftId() == null) {
            /* draft_id 为空，创建新的草稿 */

        }
        else {
            /* draft_id 不为空，更新草稿 */

        }
    }
}
