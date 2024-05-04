package team.project.module.filestorage.internal.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import team.project.module.filestorage.internal.util.Util;

@Configuration
public class AliyunOssConfig {

    @Value("${file-storage.aliyun-oss.endpoint}")
    public String endpoint;

    @Value("${file-storage.aliyun-oss.access-key-id}")
    public String accessKeyId;

    @Value("${file-storage.aliyun-oss.access-key-secret}")
    public String accessKeySecret;

    @Value("${file-storage.aliyun-oss.bucket-name}")
    public String bucketName;

    @Value("${file-storage.aliyun-oss.uploaded-files-folder}")
    public String uploadedFilesFolder;

    @Value("${file-storage.aliyun-oss.uploaded-file-id-prefix}")
    public String uploadedFileIdPrefix;

    @PostConstruct
    private void postConstruct() {
        Assert.isTrue(
               ! Util.isNotValidFilePath(uploadedFilesFolder)
            && ! Util.isNotValidFilePath(uploadedFileIdPrefix)
        , "配置文件中某项的路径配置格式不正确");
        /* 文件路径的书写规则见本模块的 package-info.java */

        /* aliyunOSS 要求路径不以斜杠开头，在这里去除斜杠（配置文件中要求路径以斜杠开头，是为了统一格式） */
        uploadedFilesFolder = uploadedFilesFolder.substring(1);
    }
}
