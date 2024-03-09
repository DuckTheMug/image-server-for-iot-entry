package com.example.demo.service;

import com.example.demo.constant.ImageProcessingConstants;
import com.example.demo.dto.NewUserDto;
import com.example.demo.exception.InvalidImageInputException;
import com.example.demo.exception.InvalidPathException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    @Getter
    private String response;
    
    public void newUser(@NonNull Image image) throws JsonProcessingException {
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

    private void validate(@NonNull String location) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NewUserDto newUserDto = new NewUserDto(location);
        ResponseEntity<String> response;
		response = new RestTemplate().postForEntity(
		        ImageProcessingConstants.VALIDATE_NEW_USER_URL,
		        new HttpEntity<>(new ObjectMapper().writeValueAsString(newUserDto), headers),
		        String.class
		);
		this.response = response.getBody();
		if (response.getStatusCode().is4xxClientError()) {
			if (Objects.equals(this.response, "User already exists."))
				throw new UserAlreadyExistsException(this.response);
			throw new InvalidImageInputException(this.response);
		}
		if (response.getStatusCode().is5xxServerError()) throw new InvalidPathException(this.response);
    }
}
