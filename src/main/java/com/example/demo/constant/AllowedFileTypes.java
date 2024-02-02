package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum AllowedFileTypes {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif");

    private final String label;

    AllowedFileTypes(String label) {
        this.label = label;
    }
}
