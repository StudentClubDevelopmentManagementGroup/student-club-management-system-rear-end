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
        /* 检测配置文件中是否存在格式不正确的项 */
        Assert.isTrue(
                uploadedFilesFolder.equals(Util.fixPath(uploadedFilesFolder))
            &&  uploadedFilesFolder.startsWith("/")
            &&  uploadedFileIdPrefix.equals(Util.fixPath(uploadedFileIdPrefix))
        , "配置文件中存在格式不正确的项");

        uploadedFilesFolder = uploadedFilesFolder.substring(1);
    }
}
