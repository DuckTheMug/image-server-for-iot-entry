package com.example.demo.service;

import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void newUser(@NonNull String name, @NonNull Image image) {
        User user = new User();
        user.setName(name);
        user.setImage(image);
        user.setEntries(new HashSet<>());
        userRepo.save(user);
    }
}
