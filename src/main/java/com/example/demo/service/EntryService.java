package com.example.demo.service;

import com.example.demo.model.Entry;
import com.example.demo.model.Image;
import com.example.demo.repo.EntryRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

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

    private Boolean validate(@NonNull String entry) {
        // TODO
        return null;
    }
}
