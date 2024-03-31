package com.example.demo.controller;

import com.example.demo.exception.StorageException;
import com.example.demo.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/master/image/{imageId}")
    public void getImage(@NonNull @PathVariable Long imageId, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            IOUtils.copy(new ByteArrayInputStream(imageService.getImageById(imageId).getImage()),
                    response.getOutputStream());
        } catch (IOException e) {
            throw new StorageException("Failed to retrieve the image.", e);
        }
    }
}
