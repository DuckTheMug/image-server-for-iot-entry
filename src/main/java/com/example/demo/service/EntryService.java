package com.example.demo.service;

import com.example.demo.constant.ImageProcessingConstants;
import com.example.demo.constant.PathConstants;
import com.example.demo.dto.ValidateEntryDto;
import com.example.demo.exception.InvalidPathException;
import com.example.demo.model.Entry;
import com.example.demo.model.Image;
import com.example.demo.repo.EntryRepo;
import com.example.demo.repo.ImageRepo;
import com.example.demo.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepo entryRepo;
    private final UserRepo userRepo;
    private final ImageRepo imageRepo;

    @Getter
    private String response;
    
    public void newEntry(@NonNull Image image) throws JsonProcessingException {
        Entry entry = new Entry();
        entry.setImage(image);
        entry.setAccessGranted(validate(image.getLocation()));
        imageRepo.findByLocation(this.response).ifPresent(i ->
                entry.setUser(userRepo.findById(i.getId()).orElse(null)));
        entryRepo.save(entry);
    }

    private @NonNull Boolean validate(@NonNull String entry) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ValidateEntryDto validateEntryDto = new ValidateEntryDto(entry, PathConstants.USER_PATH);
        ResponseEntity<String> response;
        try {
            response = new RestTemplate().postForEntity(
                    ImageProcessingConstants.VALIDATE_ENTRY_URL,
                    new HttpEntity<>(new ObjectMapper().writeValueAsString(validateEntryDto), headers),
                    String.class
            );
            this.response = response.getBody();
		    return true;
        } catch (RestClientException e) {
            assert !e.getMessage().contains("500") : "Invalid path.";
            return false;
        } catch (AssertionError e) {
            throw new InvalidPathException(e);
        }
    }
}
