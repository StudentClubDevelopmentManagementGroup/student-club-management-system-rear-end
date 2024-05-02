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
        /* 检测配置文件中是否存在格式不正确的项
           统一使用（/）作为文件夹的分隔符，以斜杠开头，不以斜杠结尾，不要出现连续的斜杠 */
        Assert.isTrue(
                uploadedFilesFolder.equals(Util.fixSeparator(uploadedFilesFolder))
            &&  uploadedFilesFolder.startsWith("/")
            &&  uploadedFileIdPrefix.equals(Util.fixSeparator(uploadedFileIdPrefix))
        , "配置文件中存在格式不正确的项");

        /* 阿里云的 OSS 存储路径规范要求不以斜杠开头，在这里去除斜杠
           （上方要求以斜杠开头，是为了统一配置文件中的路径格式） */
        uploadedFilesFolder = uploadedFilesFolder.substring(1);
    }
}
