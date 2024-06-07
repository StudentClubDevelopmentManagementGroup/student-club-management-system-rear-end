package team.project.module.club.announcement.internal.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.announcement.internal.model.entity.AnnDO;
import team.project.module.club.announcement.internal.model.entity.DraftDO;
import team.project.module.club.announcement.internal.model.query.AnnSearchQO;
import team.project.module.club.announcement.internal.model.request.AnnSearchReq;
import team.project.module.club.announcement.internal.model.view.AnnDetailVO;
import team.project.module.club.announcement.internal.model.view.DraftVO;
import team.project.module.club.announcement.tmp.ClubIdMapper;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.export.service.ManagementIService;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.service.UserInfoServiceI;

import java.util.HashSet;

@Component("club-announcement-util-ModelConverter")
public class ModelConverter {

    @Autowired
    ManagementIService clubInfoService;

    @Autowired
    UserInfoServiceI userInfoService;

    @Autowired
    ClubIdMapper clubIdMapper;

    public AnnDetailVO toAnnDetailVO(AnnDO announcementDO, String content, String summary) {
        assert announcementDO != null;

        /* TODO ljh_TODO selectClubBasicMsg 会抛异常 */
        ClubBasicMsgDTO clubInfo = clubInfoService.selectClubBasicMsg(announcementDO.getClubId());

        AnnDetailVO result = new AnnDetailVO();
        result.setAnnouncementId(announcementDO.getAnnouncementId());
        result.setDepartmentName(clubInfo.getDepartmentName());
        result.setClubName(clubInfo.getName());
        result.setAuthorName(userInfoService.getUserName(announcementDO.getAuthorId()));
        result.setPublishTime(announcementDO.getPublishTime());
        result.setTitle(announcementDO.getTitle());
        result.setContent(content);
        result.setSummary(summary);

        return result;
    }

    public DraftVO toDraftVO(DraftDO draftDO, String content, String summary) {
        assert draftDO != null;

        DraftVO result = new DraftVO();
        result.setDraftId(draftDO.getDraftId());
        result.setCreateTime(draftDO.getCreateTime());
        result.setUpdateTime(draftDO.getUpdateTime());
        result.setTitle(draftDO.getTitle());
        result.setContent(content);
        result.setSummary(summary);

        return result;
    }

    public AnnSearchQO toAnnSearchQO(AnnSearchReq searchReq) {
        assert searchReq != null;

        HashSet<Long> clubIdColl = new HashSet<>();
        if (null != searchReq.getClubId()) {
            clubIdColl.add(searchReq.getClubId());
        } else { /*  一旦指定 club_id，则忽略 club_name 和 department_id */
            if ( ! StringUtils.isBlank(searchReq.getClubName()))
                clubIdColl.addAll( clubIdMapper.searchClubByName( searchReq.getClubName() ) );
            if (null != searchReq.getDepartmentId())
                clubIdColl.addAll( clubIdMapper.searchClubByDepartmentId(searchReq.getDepartmentId()) );
        }

        HashSet<String> authorIdColl = new HashSet<>();
        if (null != searchReq.getAuthorId())
            authorIdColl.add(searchReq.getAuthorId()); /* 一旦指定 author_id，则忽略 author_name */
        else if ( ! StringUtils.isBlank(searchReq.getAuthorName())) {
            for (UserBasicInfoDTO author : userInfoService.searchUser( searchReq.getAuthorName() ))
                authorIdColl.add(author.getUserId());
        }

        AnnSearchQO searchQO = new AnnSearchQO();
        searchQO.setClubIdColl(clubIdColl);
        searchQO.setAuthorIdColl(authorIdColl);
        searchQO.setTitleKeyword(StringUtils.trimToNull(searchReq.getTitleKeyword()));
        searchQO.setFromDate(searchReq.getFromDate());
        searchQO.setToDate(searchReq.getToDate());

        return searchQO;
    }
}
