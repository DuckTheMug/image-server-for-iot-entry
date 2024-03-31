package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class RecentEntryDto {

    private Long id;

    private Boolean accessGranted;

    private Date dateTime;
}
