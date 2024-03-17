package team.project.module.filestorage.internal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

import java.io.File;

/* 测试用 */
@Component
public class ImageStorageDAO {
    Logger logger = LoggerFactory.getLogger(ImageStorageDAO.class);

    /* /resources/static */
    static final File resourceStaticFolder = new File(new ApplicationHome().getDir().getAbsolutePath(), "/src/main/resources/static");

    /* /upload/image */
    static final String redirectUrlToUploadImage = "/upload/image";

    /* /resources/static/upload/image */
    static final File uploadedImageFolder = new File(resourceStaticFolder, redirectUrlToUploadImage);

    public void uploadImageToResource(String newFileName, MultipartFile imageFile) {
        assert newFileName != null && ! newFileName.isEmpty();
        assert imageFile != null && ! imageFile.isEmpty();

        try {
            imageFile.transferTo(new File(uploadedImageFolder, newFileName));
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            throw new ServiceException(ServiceStatus.INTERNAL_SERVER_ERROR, "上传图片失败");
        }
    }

    public String getRedirectUrlToImageInResources(String imageFileName) {
        assert imageFileName != null && !imageFileName.isEmpty();

        File file = new File(uploadedImageFolder, imageFileName);
        if ( ! file.exists()) {
            return null;
        } else {
            return redirectUrlToUploadImage + "/" + imageFileName;
        }
    }
}
