package team.project.module._template.export.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.project.module._template.export.model.datatransfer.TmplDTO;
import team.project.module._template.export.model.query.TmplQO;
import team.project.module._template.export.service.TmplIService;

/* 注意接口实现类是以 "IServiceImpl" 结尾 */
@Service
public class TmplIServiceImpl implements TmplIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TmplDTO example(TmplQO queryObject) {
        /* 这里可以使用 internal 的 service */
        return null;
    }
}
