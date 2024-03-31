package com.example.demo.service;

import com.example.demo.exception.StorageException;
import com.example.demo.entity.Image;
import com.example.demo.repo.ImageRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepo imageRepo;
    public Image store(@NonNull String location, byte @NonNull [] image) {
        Image i = new Image();
        i.setLocation(location);
        i.setImage(image);
        return imageRepo.save(i);
    }
    public void deleteImage(@NonNull Image image) {
        imageRepo.delete(image);
    }

    public Image getImageById(@NonNull Long id) {
        return imageRepo.findById(id).orElseThrow(() -> new StorageException("Image doesn't exist."));
    }
}
