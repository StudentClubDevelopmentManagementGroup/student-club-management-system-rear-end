package team.project.module.club.personnelchanges.export.service;


/* 实现类记得加上 @Service */
/* 注意接口是以 "IService" 结尾 */

public interface PceIService {
    public boolean isClubManager(String userId, Long clubId);


    /* 注意这里的 DTO 和 QO 都是 export 包下的，不是 internal 包下的 */
}
