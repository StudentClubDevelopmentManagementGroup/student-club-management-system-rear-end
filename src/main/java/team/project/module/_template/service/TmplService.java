package team.project.module._template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module._template.mapper.TmplMapper;

@Service
public class TmplService {

    @Autowired
    TmplMapper tmplMapper;  /* 示例 */

    public Object list() { /* 示例 */
        return tmplMapper.list();
    }
}
