package team.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.project.module.useraccount.internal.mapper.TblUserMapper;
import team.project.module.useraccount.internal.model.entity.TblUserDO;

@SpringBootTest
class ApplicationTests {

    @Autowired
    TblUserMapper userMapper;

    @Test
    void test() {
        TblUserDO user = new TblUserDO();
        user.setUserId("1514");
        user.setDepartmentId(3L);
        user.setPassword("pppppp");
        user.setName("李某");
        user.setTel("1145141919810");
        user.setEmail("114514@guet.ooo");
        user.addRole(TblUserDO.Role.ROLE_STUDENT);
        user.addRole(TblUserDO.Role.ROLE_SUPER_ADMIN);
        user.addRole(TblUserDO.Role.ROLE_XXX);
        for (String s : user.getRoleList()) {
            System.out.println(s);
        }

        //userMapper.insert(user);
    }
}
