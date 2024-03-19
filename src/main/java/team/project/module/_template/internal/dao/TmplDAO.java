package team.project.module._template.internal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* DAO (Data Access Object) 数据访问对象
   提供对数据的持久化操作 */
@Component
public class TmplDAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /* 提供操作文件、数据库、网络等的方法，
       也可用于进一步封装 mapper（如果有需要的话） */
}
