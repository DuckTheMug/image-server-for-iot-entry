package com.example.demo.service;

import com.example.demo.exception.InvalidImageInputException;
import com.example.demo.exception.PythonException;
import com.example.demo.model.Entry;
import com.example.demo.model.GlobalPathConstants;
import com.example.demo.model.Image;
import com.example.demo.repo.EntryRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        PythonService pythonService = new PythonService(GlobalPathConstants.PYTHON_VALIDATE_ENTRY_SCRIPT, entry, GlobalPathConstants.USER_PATH);
        try {
            return switch (pythonService.exec()) {
                case 0 -> true;
                case -1 -> throw new PythonException("Image path is empty or doesn't exist.");
                case -2 -> false;
                default -> throw new PythonException("Failed to run the python script.");
            };
        } catch (IOException | InterruptedException e) {
            throw new PythonException("Failed to run the python script.", e);
        }
    }
}
