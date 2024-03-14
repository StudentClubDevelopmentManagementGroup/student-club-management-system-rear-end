package team.project.global.dao;

import org.springframework.stereotype.Component;

/* DAO (Data Access Object) 数据访问对象
   提供对数据的持久化操作 */
@Component
public class XxxDAO { /* 用到时，请重命名这个类 */

    /* 提供操作文件、数据库、网络等的方法，
       也可用于进一步封装 mapper（如果有需要的话） */

    /* 这里定义一些“所有模块会可能会使用”的方法，
       等确定某个方法会被多个模块使用后，再把它提升到 global.dao 包中 */
}
