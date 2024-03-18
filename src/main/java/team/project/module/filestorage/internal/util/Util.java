package team.project.module.filestorage.internal.util;

import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

public class Util {
    public static String generateRandomFileName(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        return UUID.randomUUID().toString().replace("-", "") + ("".equals(extension) ? "" : "." + extension);
    }
}
