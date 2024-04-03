package com.example.demo.controller;

import com.example.demo.constant.AccessPathConstants;
import com.example.demo.constant.MessageConstants;
import com.example.demo.exception.*;
import com.example.demo.constant.PathConstants;
import com.example.demo.form.NewUserForm;
import com.example.demo.entity.Image;
import com.example.demo.service.ImageService;
import com.example.demo.service.StorageService;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class UserController {
    private final ImageService imageService;
    private final UserService userService;
    private final StorageService storageService;

    @PostMapping(value = "/api/master/new-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newUser(NewUserForm newUserForm, RedirectAttributes redirectAttributes) {
        try {
            storageService.setRootPath(storageService.getRootPath().resolve(PathConstants.USER_PATH));
            storageService.store(newUserForm.getFile(), newUserForm.getName());
            userService.newUser(imageService.store(
                    PathConstants.USER_PATH + storageService.getRecentFileName(), newUserForm.getFile().getBytes()
            ), newUserForm.getName());
            storageService.flushPath(Boolean.FALSE);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        } catch (DuplicationException | NotFoundException |
                 InvalidImageInputException | InvalidPathException |
                 StorageException e) {
            storageService.flushPath(Boolean.TRUE);
            redirectAttributes.addFlashAttribute(MessageConstants.ERROR, e.getMessage());
        }
        return AccessPathConstants.REDIRECT_NEW_USER;
    }

    @DeleteMapping("/api/master/delete-user")
    public String deleteUser(@NonNull @RequestBody Long id, RedirectAttributes redirectAttributes) {
        try {
            Image toBeDeleted = userService.findUserById(id).getImage();
            storageService.softDelete(toBeDeleted.getLocation());
            imageService.deleteImage(toBeDeleted);
            userService.deleteUser(id);
        } catch (NotFoundException | StorageException e) {
            storageService.flushPath(Boolean.TRUE);
            redirectAttributes.addFlashAttribute(MessageConstants.ERROR, e.getMessage());
        }
        return AccessPathConstants.REDIRECT_USER;
    }

    @GetMapping("/master/user")
    public String initUser(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return AccessPathConstants.USER;
    }

    @GetMapping("/master/new-user")
    public String initNewUser() {
        return AccessPathConstants.NEW_USER;
    }
}
