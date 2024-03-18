package team.project.module.filestorage.internal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class FileStorageConfig implements WebMvcConfigurer {
    @Value("${file-storage.local-file-system.root-folder}")
    public String rootFolder;

    @Value("${file-storage.local-file-system.uploaded-files-folder}")
    public String uploadedFilesFolder;

    @Value("${file-storage.local-file-system.uploaded-files-resource-url-prefix}")
    public String uploadedFilesResourceUrlPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /* 定义静态资源映射目录，注意映射的文件路径要以 file: 开头，以 / 结尾 */
        registry.addResourceHandler(uploadedFilesResourceUrlPrefix + "/**")
                .addResourceLocations("file:" + rootFolder + "/" + uploadedFilesFolder+ "/");
    }

    @Value("${file-storage.aliyun-oss.endpoint}")
    public String aliyunOssEndpoint;

    @Value("${file-storage.aliyun-oss.access-key-id}")
    public String aliyunOssAccessKeyId;

    @Value("${file-storage.aliyun-oss.access-key-secret}")
    public String aliyunOssAccessKeySecret;

    @Value("${file-storage.aliyun-oss.bucket-name}")
    public String aliyunOssBucketName;
}
