package team.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootTest
class ApplicationTests {

    @Autowired
    LocalFileSystemDAO dao;

    @Test
    void test() throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}
