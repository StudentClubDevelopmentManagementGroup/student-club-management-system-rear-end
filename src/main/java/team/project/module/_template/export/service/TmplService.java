package team.project.module._template.export.service;

import org.springframework.stereotype.Service;
import team.project.module._template.export.model.datatransfer.TmplDTO;
import team.project.module._template.export.model.query.TmplQO;

/* 实现类记得加上 @Service */
@Service
public interface TmplService {
    TmplDTO example(TmplQO queryObject);
    /* 注意这里的 DTO 和 QO 都是 export 包下的，不是 internal 包下的 */
}
