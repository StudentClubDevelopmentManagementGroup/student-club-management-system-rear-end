package team.project.module.filestorage.internal.dao;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.config.AliyunOssConfig;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

/* 对象（Object）是 OSS 存储数据的基本单元，也被称为OSS的文件
   和传统的文件系统不同，Object没有文件目录层级结构的关系，OSS通过键名（Key）唯一标识存储的Object
   Object的命名规范如下：
    - 使用UTF-8编码
    - 长度必须在1~1023字符之间
    - 不能以正斜线（/）或者反斜线（\）开头
    - 区分大小写

    默认情况下，OSS存储空间中文件的读写权限是私有，仅文件拥有者具有访问文件的权限
    但是，文件拥有者可以通过创建签名URL的方式与第三方用户分享文件，
    签名URL使用安全凭证的方式授权第三方用户在指定时间内下载或者预览文件
*/

@Component
public class AliyunOssStorageDAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String bucketName;
    private final OSS    ossClient;

    AliyunOssStorageDAO(AliyunOssConfig cfg) {
        String endpoint        = cfg.endpoint;
        String accessKeyId     = cfg.accessKeyId;
        String accessKeySecret = cfg.accessKeySecret;
        this.bucketName        = cfg.bucketName;

        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 上传文件（采用简单上传的方式，上传不超过5 GB大小的文件）
     * */
    public void upload(String key, MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream());
        PutObjectResult result = ossClient.putObject(putObjectRequest);
    }

    /**
     * 获取访问文件的 URL（fileId 指向的文件不存在也会返回 URL，访问这个 URL 会响应文件不存在）
     * */
    public String getUrl(String key) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);
        request.setExpiration(new Date(new Date().getTime() + 10 * 1000L)); /* 设置过期时间 10 分钟 */

        URL url = ossClient.generatePresignedUrl(request);
        return url.toString();
    }

    /**
     * 删除文件
     * */
    public void delete(String key) {
        ossClient.deleteObject(bucketName, key);
    }
}
