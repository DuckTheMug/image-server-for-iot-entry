package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @NonNull
    private Long id;

    @NonNull
    @NotEmpty
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @NonNull
    @JoinColumn(name = "image_id", referencedColumnName = "id", nullable = false, unique = true)
    private Image image;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_entry",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "entry_id")})
    private Set<Entry> entries = new HashSet<>();
}
