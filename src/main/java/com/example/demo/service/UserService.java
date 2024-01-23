package com.example.demo.service;

import com.example.demo.exception.InvalidImageInputException;
import com.example.demo.exception.PythonException;
import com.example.demo.model.GlobalPathConstants;
import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void newUser(@NonNull Image image) {
        validate(image.getLocation());
        User user = new User();
        user.setName(FilenameUtils.getBaseName(image.getLocation()));
        user.setImage(image);
        user.setEntries(new HashSet<>());
        userRepo.save(user);
    }
    public void deleteUser(@NonNull String name) {
        userRepo.deleteByName(name);
    }

    public User findUserByName(@NonNull String name) {
        return userRepo.findByName(name);
    }

    private void validate(@NonNull String location) {
        PythonService pythonService = new PythonService(GlobalPathConstants.PYTHON_NEW_USER_SCRIPT, location);
        try {
            switch (pythonService.exec()) {
                case 0 -> {}
                case -1 -> throw new PythonException("Image path is empty or doesn't exist.");
                case -2 ->
                        throw new InvalidImageInputException("The picture has no valid face or more than 1 valid face.");
                default -> throw new PythonException("Failed to run the python script.");
            }
        } catch (IOException | InterruptedException e) {
            throw new PythonException("Failed to run the python script.", e);
        }
    }
}
