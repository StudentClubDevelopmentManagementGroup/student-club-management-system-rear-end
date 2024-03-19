package team.project.module.filestorage.internal.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.project.module.filestorage.internal.util.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class LocalFileSystemConfig implements WebMvcConfigurer {
    @Value("${file-storage.local-file-system.root-folder}")
    public String rootFolder;

    @Value("${file-storage.local-file-system.uploaded-files-folder}")
    public String uploadedFilesFolder;

    @Value("${file-storage.local-file-system.uploaded-file-id-prefix}")
    public String uploadedFileIdPrefix;

    public String baseUrl;

    @Value("${server.port}")
    private String port;

    @PostConstruct
    private void init() throws UnknownHostException {
        assert rootFolder.equals(Util.fixPath(rootFolder))
            && ! uploadedFilesFolder.endsWith("/");

        assert uploadedFilesFolder.equals(Util.fixPath(uploadedFilesFolder))
            && uploadedFilesFolder.startsWith("/")
            && ! uploadedFilesFolder.endsWith("/");

        assert uploadedFileIdPrefix.equals(Util.fixPath(uploadedFileIdPrefix))
            && uploadedFilesFolder.startsWith("/")
            && ! uploadedFileIdPrefix.endsWith("/");

        baseUrl = "http://" +  InetAddress.getLocalHost().getHostAddress() + ":" + port;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /* 定义静态资源映射目录，注意映射的文件路径要以 file: 开头，以 / 结尾 */
        registry.addResourceHandler(uploadedFilesFolder + "/**")
                .addResourceLocations("file:" + rootFolder + uploadedFilesFolder+ "/");
    }
}
