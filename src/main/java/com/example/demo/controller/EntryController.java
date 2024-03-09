package com.example.demo.controller;

import com.example.demo.constant.PathConstants;
import com.example.demo.exception.StorageException;
import com.example.demo.service.EntryService;
import com.example.demo.service.ImageService;
import com.example.demo.service.StorageService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class EntryController {
    private final EntryService entryService;
    private final StorageService storageService;
    private final ImageService imageService;

    @PostMapping("/api/master/new_entry")
    public ResponseEntity<String> newEntry(@NonNull @RequestParam MultipartFile file) {
        try {
            storageService.setRootPath(storageService.getRootPath().resolve(PathConstants.ENTRY_PATH));
            storageService.store(file, null);
            entryService.newEntry(imageService.store(PathConstants.ENTRY_PATH + storageService.getRecentFileName(), file.getBytes()));
            storageService.flushPath();
            return ResponseEntity.status(HttpStatus.OK).body("Register new entry successfully.");
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }
}
