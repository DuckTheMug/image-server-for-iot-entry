package com.example.demo.util;

import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomFileNameUtil {
    public static @NonNull String randomFileName(@NonNull String extension) {
        return "%s%s%s%s".formatted(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date(System.currentTimeMillis())),
                RandomStringUtils.randomNumeric(9),
                ".",
                extension
        );
    }
}
