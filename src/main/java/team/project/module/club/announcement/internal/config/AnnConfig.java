package team.project.module.club.announcement.internal.config;

import team.project.module.util.filestorage.export.model.enums.FileStorageType;

import static team.project.module.util.filestorage.export.model.enums.FileStorageType.CLOUD;

public class AnnConfig {
    public static final FileStorageType STORAGE_TYPE_FILE  = CLOUD;
    public static final FileStorageType STORAGE_TYPE_ANN   = CLOUD;
    public static final FileStorageType STORAGE_TYPE_DRAFT = CLOUD;
    public static final String FOLDER_FILE  = "/club/announcement/file";
    public static final String FOLDER_ANN   = "/club/announcement/announcement";
    public static final String FOLDER_DRAFT = "/club/announcement/announcement";
}
