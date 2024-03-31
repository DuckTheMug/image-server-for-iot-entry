package com.example.demo.entity;

import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "image")
@SQLDelete(sql = "UPDATE DEMO.IMAGE SET deleted = TRUE WHERE ID = ?")
@FilterDef(name = "deletedImageFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedImageFilter", condition = "deleted = :isDeleted")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @NonNull
    private Long imageId;

    @NonNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "image", length = Integer.MAX_VALUE, nullable = false)
    @Lob
    private byte @NonNull [] image;

    @Column(name = "deleted", nullable = false)
    @NonNull
    private Boolean deleted = Boolean.FALSE;
}
