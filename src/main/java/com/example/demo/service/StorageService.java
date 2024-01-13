package com.example.demo.service;

import com.example.demo.exception.IllegalFileTypeException;
import com.example.demo.exception.StorageException;
import com.example.demo.model.AllowedFileType;
import com.example.demo.model.Storage;
import com.example.demo.util.RandomFileNameUtil;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class StorageService {
    private final Path rootPath;

    @Getter
    private String recentFileName;

    public StorageService(@NonNull @Autowired Storage storage) {
        if (storage.getPath().isBlank())
            throw new StorageException();
        this.rootPath = Paths.get(storage.getPath().strip());
        if (!this.rootPath.toFile().exists()) {
            try {
                Files.createDirectories(this.rootPath);
            } catch (IOException e) {
                throw new StorageException("Can't not initialize the root path directory", e);
            }
        }
    }

    public void store(@NonNull MultipartFile file) {
        @NonNull String ext;
        //Checks if the file type is valid
        try {
             ext = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (NullPointerException e) {
            throw new IllegalFileTypeException("Only JPG, JPEG, PNG or GIF file is allowed.");
        }
        for (int i = 0; i < AllowedFileType.values().length; i++) {
            if (ext.equals(AllowedFileType.values()[i].name()))
                break;
            else if (i == AllowedFileType.values().length - 1)
                throw new IllegalFileTypeException("Only JPG, JPEG, PNG or GIF file is allowed.");
        }
        String previousFileName = this.recentFileName;
        try {
            this.recentFileName = RandomFileNameUtil.randomFileName(ext);
            Path toStoreFile = this.rootPath.resolve(Paths.get(this.recentFileName)).normalize().toAbsolutePath();
//            if (!toStoreFile.getParent().equals(this.rootPath.toAbsolutePath())) {
//                throw new StorageException("Can't store file outside the root path.");
//            }
            try (InputStream stream = file.getInputStream()) {
                Files.copy(stream, toStoreFile);
            }
        } catch (IOException e) {
            this.recentFileName = previousFileName;
            throw new StorageException("Failed to store file.", e);
        }
    }
}
