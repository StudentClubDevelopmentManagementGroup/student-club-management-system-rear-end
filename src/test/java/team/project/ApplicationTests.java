package team.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.Role;
import team.project.module.user.export.service.impl.UserInfoIServiceImpl;

@SpringBootTest
class ApplicationTests {

    @Autowired
    UserInfoIServiceImpl userInfoIService;

    @Test
    void test() {
        UserInfoDTO ljh = userInfoIService.getUserInfoByUserId("2100301514");
        System.out.println(ljh);
        System.out.println(ljh.hasRole(Role.CLUB_LEADER));
        System.out.println(ljh.getRoleList());
    }
}
