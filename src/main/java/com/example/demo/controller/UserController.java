package com.example.demo.controller;

import com.example.demo.exception.*;
import com.example.demo.constant.PathConstants;
import com.example.demo.model.Image;
import com.example.demo.service.ImageService;
import com.example.demo.service.StorageService;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class UserController {
    private final ImageService imageService;
    private final UserService userService;
    private final StorageService storageService;

    @PostMapping(value = "/api/master/new_user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<String> newUser(@NonNull @RequestPart MultipartFile file,
                                                        @NonNull @RequestPart String name) {
        try {
            storageService.setRootPath(storageService.getRootPath().resolve(PathConstants.USER_PATH));
            storageService.store(file, name);
            userService.newUser(imageService.store(
                    PathConstants.USER_PATH + storageService.getRecentFileName(), file.getBytes()
            ), name);
            storageService.flushPath(Boolean.FALSE);
            return ResponseEntity.status(HttpStatus.OK).body("Register new user successfully.");
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @DeleteMapping("/api/master/delete_user")
    public @ResponseBody ResponseEntity<String> deleteUser(@NonNull @RequestBody String name) {
        try {
            Image toBeDeleted = userService.findUserByName(name).getImage();
            storageService.softDelete(toBeDeleted.getLocation());
            imageService.deleteImage(toBeDeleted);
            userService.deleteUser(name);
            return ResponseEntity.status(HttpStatus.OK).body("Delete user successfully.");
        } catch (NullPointerException e) {
            throw new UserDoesNotExistException("User doesn't exist.", e);
        }
    }
}
