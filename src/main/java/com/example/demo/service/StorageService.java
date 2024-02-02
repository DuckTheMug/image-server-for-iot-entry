package com.example.demo.service;

import com.example.demo.exception.IllegalFileTypeException;
import com.example.demo.exception.StorageException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.constant.AllowedFileTypes;
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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class StorageService {
    @Getter
    private Path rootPath;

    @Getter
    private String recentFileName;

    private String defaultPath;

    public StorageService(@NonNull @Autowired Storage storage) {
        if (storage.getPath().isBlank())
            throw new StorageException();
        this.defaultPath = storage.getPath().strip();
        this.rootPath = Paths.get(this.defaultPath);
        if (!this.rootPath.toFile().exists()) {
            try {
                this.init();
            } catch (StorageException e) {
                this.defaultPath = null;
                this.rootPath = null;
                throw e;
            }
        }
    }

    public void store(@NonNull MultipartFile file, String name) {
        @NonNull String ext;
        // Checks if the file type is valid
        try {
             ext = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (NullPointerException e) {
            throw new IllegalFileTypeException("Only JPG, JPEG, PNG or GIF file is allowed.");
        }
        for (int i = 0; i < AllowedFileTypes.values().length; i++) {
            if (ext.equals(AllowedFileTypes.values()[i].getLabel()))
                break;
            else if (i == AllowedFileTypes.values().length - 1)
                throw new IllegalFileTypeException("Only JPG, JPEG, PNG or GIF file is allowed.");
        }
        String previousFileName = this.recentFileName;
        try {
            // if there is no specific filename, the filename would be randomized (for new entries)
            // if there is a specific filename, the filename would be set accordingly (for new users)
            this.recentFileName = (name == null) ? RandomFileNameUtil.randomFileName(ext) : name + "." + ext;
            Path toStoreFile = this.rootPath.resolve(Paths.get(this.recentFileName)).normalize().toAbsolutePath();
            try (InputStream stream = file.getInputStream()) {
                Files.copy(stream, toStoreFile);
            }
        } catch (FileAlreadyExistsException e) {
            this.recentFileName = previousFileName;
            throw new UserAlreadyExistsException("User already exists.", e);
        } catch (IOException e) {
            this.recentFileName = previousFileName;
            throw new StorageException("Failed to store file.", e);
        }
    }

    public void delete(String location) {
        try {
            Files.deleteIfExists(this.rootPath.resolve(location));
        } catch (IOException e) {
            throw new StorageException("Failed to delete file.", e);
        }
    }

    public void flushPath() {
        this.rootPath = Paths.get(this.defaultPath);
    }

    public void init() {
        try {
            Files.createDirectories(this.rootPath);
        } catch (IOException e) {
            throw new StorageException("Can't not initialize the root path directory", e);
        }
    }

    public void setRootPath(@NonNull Path rootPath) {
        this.rootPath = rootPath;
        if (!this.rootPath.toFile().exists())
            this.init();
    }
}
