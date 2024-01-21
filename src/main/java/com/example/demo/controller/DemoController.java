package com.example.demo.controller;

import com.example.demo.exception.*;
import com.example.demo.model.GlobalPathConstants;
import com.example.demo.model.Image;
import com.example.demo.service.EntryService;
import com.example.demo.service.ImageService;
import com.example.demo.service.StorageService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class DemoController {
    private final EntryService entryService;
    private final ImageService imageService;
    private final UserService userService;
    private final StorageService storageService;

    @PostMapping("/newuser")
    public ResponseEntity<String> newUser(@NonNull @RequestParam MultipartFile file, @NonNull @RequestParam String name) {
        try {
            storageService.setRootPath(storageService.getRootPath().resolve(GlobalPathConstants.USER_PATH));
            storageService.store(file, name);
            userService.newUser(storageService.getRecentFileName(), imageService.store(GlobalPathConstants.USER_PATH + storageService.getRecentFileName(), file.getBytes()));
            storageService.flushPath();
            return ResponseEntity.status(HttpStatus.OK).body("Register new user successfully.");
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @PostMapping("/deleteuser")
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
            PythonException.class
    })
    public ResponseEntity<String> handleInternalException(@NonNull RuntimeException e) {
        storageService.flushPath();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
