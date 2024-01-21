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

    public void newUser(@NonNull String filename, @NonNull Image image){
        User user = new User();
        user.setName(FilenameUtils.getBaseName(filename));
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
}
