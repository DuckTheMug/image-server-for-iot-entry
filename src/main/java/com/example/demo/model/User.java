package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "user")
@SQLDelete(sql = "UPDATE DEMO.USER SET DELETED = TRUE WHERE ID = ?")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "deleted", type = Boolean.class))
@Filter(name = "deletedUserFilter", condition = "deleted = :deleted")
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
    @JoinColumn(name = "image_id", referencedColumnName = "id", unique = true)
    private Image image;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            orphanRemoval = true, targetEntity = Entry.class,cascade = CascadeType.ALL)
    private Set<Entry> entries = new HashSet<>();

    @Column(name = "deleted", nullable = false)
    @NonNull
    private Boolean deleted = Boolean.FALSE;
}
