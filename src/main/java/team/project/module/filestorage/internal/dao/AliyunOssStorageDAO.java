package team.project.module.filestorage.internal.dao;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.config.FileStorageConfig;

import java.io.IOException;

@Component
public class AliyunOssStorageDAO {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    AliyunOssStorageDAO(FileStorageConfig cfg) {
        this.endpoint        = cfg.aliyunOssEndpoint;
        this.accessKeyId     = cfg.aliyunOssAccessKeyId;
        this.accessKeySecret = cfg.aliyunOssAccessKeySecret;
        this.bucketName      = cfg.aliyunOssBucketName;
    }

    public void upload(String targetFilepath, MultipartFile file) throws IOException{

        /* 将文件路径中的多个连续'/' 替换为一个'/' */
        targetFilepath = targetFilepath.replaceAll("/+", "/");

        /* DAO 向外公布的接口中，文件夹统一以'/' 开头
           但 OSS 文件夹统一以'/' 结尾（上传的文件开头不能是'/'） */
        if (targetFilepath.startsWith("/")) {
            targetFilepath = targetFilepath.substring(1);
        }

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, targetFilepath, file.getInputStream());
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
