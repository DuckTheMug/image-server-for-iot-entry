package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @NonNull
    private Long id;

    @NonNull
    @Column(name = "date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @NonNull
    @Column(name = "location", nullable = false, unique = true)
    private String location;

    @Column(name = "image", length = Integer.MAX_VALUE, nullable = false)
    @Lob
    private byte @NonNull [] image;
}
