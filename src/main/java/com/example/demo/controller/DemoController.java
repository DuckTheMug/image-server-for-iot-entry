package com.example.demo.controller;

import com.example.demo.exception.*;
import com.example.demo.constant.PathConstants;
import com.example.demo.model.Image;
import com.example.demo.service.EntryService;
import com.example.demo.service.ImageService;
import com.example.demo.service.StorageService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class DemoController {
    private final EntryService entryService;
    private final ImageService imageService;
    private final UserService userService;
    private final StorageService storageService;

    @PostMapping("/api/new_user")
    public ResponseEntity<String> newUser(@NonNull @RequestParam MultipartFile file, @NonNull @RequestParam String name) {
        try {
            storageService.setRootPath(storageService.getRootPath().resolve(PathConstants.USER_PATH));
            storageService.store(file, name);
            userService.newUser(imageService.store(PathConstants.USER_PATH + storageService.getRecentFileName(), file.getBytes()));
            storageService.flushPath();
            return ResponseEntity.status(HttpStatus.OK).body("Register new user successfully.");
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @DeleteMapping("/api/delete_user")
    public ResponseEntity<String> deleteUser(@NonNull @RequestParam String name) {
        try {
            Image toBeDeleted = userService.findUserByName(name).getImage();
            storageService.delete(toBeDeleted.getLocation());
            imageService.deleteImage(toBeDeleted);
            userService.deleteUser(name);
            return ResponseEntity.status(HttpStatus.OK).body("Delete user successfully.");
        } catch (NullPointerException e) {
            throw new UserDoesNotExistException("User doesn't exist.", e);
        }
    }

    @PostMapping("/api/new_entry")
    public ResponseEntity<String> newEntry(@NonNull @RequestParam MultipartFile file) {
        try {
            storageService.setRootPath(storageService.getRootPath().resolve(PathConstants.ENTRY_PATH));
            storageService.store(file, null);
            Boolean accessGranted = entryService.newEntry(imageService.store(PathConstants.ENTRY_PATH + storageService.getRecentFileName(), file.getBytes())).getAccessGranted();
            if (accessGranted) {
                // Send POST request to ESP32-CAM
            } else {
                // Send POST request to ESP32-CAM
            }
            storageService.flushPath();
            return ResponseEntity.status(HttpStatus.OK).body("Register new entry successfully.");
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @ExceptionHandler({
            IllegalFileTypeException.class,
            InvalidImageInputException.class,
            UserAlreadyExistsException.class,
            UserDoesNotExistException.class
    })
    public ResponseEntity<String> handleIllegalFileException(@NonNull RuntimeException e) {
        storageService.flushPath();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler({
            StorageException.class,
            InvalidPathException.class
    })
    public ResponseEntity<String> handleInternalException(@NonNull RuntimeException e) {
        storageService.flushPath();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
