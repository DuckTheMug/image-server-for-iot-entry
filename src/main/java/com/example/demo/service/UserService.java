package com.example.demo.service;

import com.example.demo.constant.ImageProcessingConstants;
import com.example.demo.dto.NewUserDto;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.*;
import com.example.demo.entity.Image;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    @Getter
    private String response;

    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream().map(user ->
                ObjectUtils.anyNull(user.getUserId(), user.getName(), user.getImage()) ? null :
                new UserDto(user.getUserId(), user.getName(), user.getImage().getImage())
        ).toList();
    }

    public void newUser(@NonNull Image image, @NonNull String name) throws JsonProcessingException {
        validate(image.getLocation());
        User user = new User();
        user.setName(name);
        user.setImage(image);
        user.setEntries(new HashSet<>());
        userRepo.save(user);
    }
    public void deleteUser(@NonNull Long id) {
        userRepo.delete(userRepo.findById(id).orElseThrow(() -> new NotFoundException("User doesn't exist.")));
    }

    public User findUserById(@NonNull Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User doesn't exist."));
    }

    private void validate(@NonNull String location) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NewUserDto newUserDto = new NewUserDto(location);
        ResponseEntity<String> response;
        try {
            response = new RestTemplate().postForEntity(
                    ImageProcessingConstants.VALIDATE_NEW_USER_URL,
                    new HttpEntity<>(new ObjectMapper().writeValueAsString(newUserDto), headers),
                    String.class
            );
            this.response = response.getBody();
        } catch (RestClientException e) {
            assert !e.getMessage().contains("500") : e;
            assert !e.getMessage().contains("415") : e;
            throw new UserAlreadyExistsException(e);
        } catch (AssertionError e) {
            throw new InvalidPathException(e);
        }
    }
}
