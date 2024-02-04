package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    private String img;

    @Override
    public String toString() {
        return "{" +
                "\"img\":\"" + this.getImg() + '\"' +
                '}';
    }
}
