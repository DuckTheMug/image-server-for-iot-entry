package com.example.demo.service;

import com.example.demo.constant.ImageProcessingConstants;
import com.example.demo.constant.PathConstants;
import com.example.demo.dto.ValidateEntryDto;
import com.example.demo.exception.InvalidPathException;
import com.example.demo.model.Entry;
import com.example.demo.model.Image;
import com.example.demo.repo.EntryRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class EntryService {
    private final EntryRepo entryRepo;

    public Entry newEntry(@NonNull Image image) {
        Entry entry = new Entry();
        entry.setImage(image);
        entry.setAccessGranted(validate(image.getLocation()));
        return entryRepo.save(entry);
    }

    private @NonNull Boolean validate(@NonNull String entry) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ValidateEntryDto validateEntryDto = new ValidateEntryDto(entry, PathConstants.ENTRY_PATH);
        ResponseEntity<String> response = new RestTemplate().postForEntity(
                ImageProcessingConstants.VALIDATE_ENTRY_URL,
                new HttpEntity<>(validateEntryDto.toString(), headers),
                String.class
        );
        if (response.getStatusCode().is2xxSuccessful()) return true;
        if (response.getStatusCode().is4xxClientError()) return false;
        throw new InvalidPathException(response.getBody());
    }
}
