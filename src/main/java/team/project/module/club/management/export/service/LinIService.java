package team.project.module.club.management.export.service;

import team.project.module.club.management.export.model.datatransfer.LinDTO;
import team.project.module.club.management.export.model.query.LinQO;

/* 实现类记得加上 @Service */
/* 注意接口是以 "IService" 结尾 */
public interface LinIService {
    LinDTO example(LinQO queryObject);
    /* 注意这里的 DTO 和 QO 都是 export 包下的，不是 internal 包下的 */
}
