package team.project.module.filestorage.export.util;

import java.util.UUID;

public class FileStorageUtil {
    public static String randomFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
