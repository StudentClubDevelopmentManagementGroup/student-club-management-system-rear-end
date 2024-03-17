package team.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.project.module.filestorage.internal.config.FileStorageConfig;
import team.project.module.filestorage.internal.dao.LocalFileSystemDAO;

@SpringBootTest
class ApplicationTests {

    @Autowired
    LocalFileSystemDAO dao;

    @Test
    void test() {
    }
}
