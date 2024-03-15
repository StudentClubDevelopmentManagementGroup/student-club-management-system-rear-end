package team.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    @Test
    void test() {
        System.out.println(new ApplicationHome().getDir());
    }
}
