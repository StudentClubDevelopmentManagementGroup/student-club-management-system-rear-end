package team.project.module.filestorage.internal.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import team.project.module.filestorage.internal.util.Util;

import java.net.UnknownHostException;

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
    private void init() throws UnknownHostException {
        assert uploadedFilesFolder.equals(Util.fixPath(uploadedFilesFolder))
            && uploadedFilesFolder.startsWith("/")
            && ! uploadedFilesFolder.endsWith("/");
        uploadedFilesFolder = uploadedFilesFolder.substring(1);

        assert uploadedFileIdPrefix.equals(Util.fixPath(uploadedFileIdPrefix))
            && ! uploadedFileIdPrefix.endsWith("/");
    }
}
