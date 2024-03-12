package com.example.demo.controller.advice;

import com.example.demo.exception.*;
import com.example.demo.service.StorageService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@AllArgsConstructor
public class DemoControllerAdvice extends ResponseEntityExceptionHandler {
    private final StorageService storageService;

    @ExceptionHandler({
            IllegalFileTypeException.class,
            InvalidImageInputException.class,
            UserAlreadyExistsException.class,
            UserDoesNotExistException.class
    })
    public ResponseEntity<String> handleIllegalFileException(@NonNull RuntimeException e) {
        storageService.flushPath(Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler({
            StorageException.class,
            InvalidPathException.class
    })
    public ResponseEntity<String> handleInternalException(@NonNull RuntimeException e) {
        storageService.flushPath(Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
