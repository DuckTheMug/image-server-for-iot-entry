package com.example.demo.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource("classpath:application.properties")
public class Storage {
    @NonNull
    @Value("${storage.path}")
    private String path;
}
