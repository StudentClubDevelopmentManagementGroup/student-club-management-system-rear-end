package team.project.module._template.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module._template.internal.mapper.TmplMapper;

@Service
public class TmplService {


    TmplMapper tmplMapper;  /* 示例 */

    public Object list() { /* 示例 */
        return tmplMapper.list();
    }
}
