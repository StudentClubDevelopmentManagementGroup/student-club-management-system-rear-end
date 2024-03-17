package team.project.module.manage_lin.export.service.impl;

import org.springframework.stereotype.Service;
import team.project.module._template.export.model.datatransfer.TmplDTO;
import team.project.module._template.export.model.query.TmplQO;
import team.project.module._template.export.service.TmplService;

@Service
public class TmplServiceImpl implements TmplService {
    public TmplDTO example(TmplQO queryObject) {
        /* 这里可以使用 internal 的 service */
        return null;
    }
}
