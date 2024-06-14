package team.project.module.club.announcement.config;

import team.project.module.util.filestorage.export.model.enums.FileStorageType;

import static team.project.module.util.filestorage.export.model.enums.FileStorageType.LOCAL;

public class AnnConfig {
    public static final FileStorageType STORAGE_TYPE_FILE  = LOCAL;
    public static final FileStorageType STORAGE_TYPE_ANN   = LOCAL;
    public static final FileStorageType STORAGE_TYPE_DRAFT = LOCAL;
    public static final String FOLDER_FILE  = "/club/announcement/file";
    public static final String FOLDER_ANN   = "/club/announcement/announcement";
    public static final String FOLDER_DRAFT = "/club/announcement/announcement";
}
