package com.example.demo.service;

import com.example.demo.constant.ImageProcessingConstants;
import com.example.demo.dto.NewUserDto;
import com.example.demo.exception.InvalidImageInputException;
import com.example.demo.exception.InvalidPathException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Objects;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NewUserDto newUserDto = new NewUserDto(location);
        ResponseEntity<String> response = new RestTemplate().postForEntity(
                ImageProcessingConstants.VALIDATE_NEW_USER_URL,
                new HttpEntity<>(newUserDto.toString(), headers),
                String.class
        );
        if (response.getStatusCode().is4xxClientError()) {
            if (Objects.equals(response.getBody(), "User already exists."))
                throw new UserAlreadyExistsException(response.getBody());
            throw new InvalidImageInputException(response.getBody());
        }
        if (response.getStatusCode().is5xxServerError()) throw new InvalidPathException(response.getBody());
    }
}
