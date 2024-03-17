package team.project.module.filestorage.internal.service;

import cn.hutool.core.lang.UUID;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.filestorage.internal.dao.ImageStorageDAO;

@Service
public class ImageStorageService {

    @Autowired
    ImageStorageDAO dao;

    public String uploadImageToResource(MultipartFile imageFile) {

        String originFilename = imageFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originFilename);
        String newFilename = String.format("%s.%s", UUID.randomUUID(), extension);

        dao.uploadImageToResource(newFilename, imageFile);

        return newFilename;
    }

    public String getRedirectUrlToImageFileInResources(String imageFileName) {
        return dao.getRedirectUrlToImageInResources(imageFileName);
    }
}
