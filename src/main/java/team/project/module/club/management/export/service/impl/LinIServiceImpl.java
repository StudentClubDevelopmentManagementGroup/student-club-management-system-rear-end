package team.project.module.club.management.export.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.project.module.club.management.export.model.datatransfer.LinDTO;
import team.project.module.club.management.export.model.query.LinQO;
import team.project.module.club.management.export.service.LinIService;

/* 注意接口实现类是以 "IServiceImpl" 结尾 */
@Service
public class LinIServiceImpl implements LinIService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public LinDTO example(LinQO queryObject) {
        /* 这里可以使用 internal 的 service */
        return null;
    }
}
