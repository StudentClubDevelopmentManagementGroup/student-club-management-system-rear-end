package team.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRole;

@SpringBootTest
class ApplicationTests {

    @Autowired
    TblUserMapper userMapper;

    @Test
    void test() {
        TblUserDO tblUserDO = new TblUserDO();
        tblUserDO.setRole(UserRole.getEmptyRoleCode());
        tblUserDO.addRole(UserRole.STUDENT);
        tblUserDO.addRole(UserRole.TEACHER);
        tblUserDO.addRole(UserRole.CLUB_LEADER);
    }
}
