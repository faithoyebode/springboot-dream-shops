package com.faithoyebode.dreamshops.controller;

import com.faithoyebode.dreamshops.dto.ImageDto;
import com.faithoyebode.dreamshops.model.Image;
import com.faithoyebode.dreamshops.response.ApiResponse;
import com.faithoyebode.dreamshops.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload Success!", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed!",  e.getMessage()));
        }
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<Object> downloadImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getBinaryData().getBytes(1, (int) image.getBinaryData().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unable to download image!",  e.getMessage()));
        }
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file){
        try {
            imageService.updateImage(file, imageId);
            return ResponseEntity.ok(new ApiResponse("Update success!", null));
        } catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                   .body(new ApiResponse(e.getMessage(), null));
        }

    }


    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Delete success!", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }

    }






}
