package com.faithoyebode.dreamshops.service.image;

import com.faithoyebode.dreamshops.dto.ImageDto;
import com.faithoyebode.dreamshops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
