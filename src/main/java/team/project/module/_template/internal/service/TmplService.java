package team.project.module._template.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module._template.internal.mapper.TmplMapper;

@Service
public class TmplService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TmplMapper tmplMapper;  /* 示例 */

    public Object list() { /* 示例 */
        return tmplMapper.list();
    }
}
