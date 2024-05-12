package team.project.module.util.filestorage.export.util;

import java.util.UUID;

public class FileStorageUtil {
    public static String randomFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
