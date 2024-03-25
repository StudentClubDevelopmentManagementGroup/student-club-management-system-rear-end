package team.project.module._template.export.service;

import team.project.module._template.export.model.datatransfer.TmplDTO;
import team.project.module._template.export.model.query.TmplQO;

/* 实现类记得加上 @Service */
/* 注意接口是以 "IService" 结尾 */
public interface TmplIService {
    TmplDTO example(TmplQO queryObject);
    /* 注意这里的 DTO 和 QO 都是 export 包下的，不是 internal 包下的 */
}
