package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidateEntryDto {
    private String img;
    private String db;

    @Override
    public String toString() {
        return '{' +
                "\"img\":\"" + this.getImg() + '\"' +
                ",\"db\":\"" + this.getDb() + '\"' +
                '}';
    }
}
