package com.example.demo.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Data
public class NewUserForm {
    @NonNull
    @NotNull
    private MultipartFile file;
    @NonNull
    @NotEmpty
    private String name;
    @NonNull
    @NotNull
    private Long lockId;
}
