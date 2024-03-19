package team.project.module._template.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module._template.internal.mapper.TmplMapper;
import team.project.module._template.internal.mapper.TmplMapper2;

@Service
public class TmplService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TmplMapper tmplMapper;    /* mybatis（java 接口与 xml SQL） */

    @Autowired
    TmplMapper2 tmplMapper2;  /* mybatis-plus（只配 java 接口） */

    public Object list() { /* 示例 */
        return tmplMapper.list();
    }

    public Object list2() { /* 示例 */
        return tmplMapper2.selectList(null);
    }
}
