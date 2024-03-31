package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockInfoDto {

    private Long lockId;

    private String lockName;

    private Boolean lockState;
}
