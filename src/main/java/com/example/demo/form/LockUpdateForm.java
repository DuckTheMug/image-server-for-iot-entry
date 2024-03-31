package com.example.demo.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LockUpdateForm {
    private Long lockId;
    private String lockName;
    private Boolean lockState;
}
