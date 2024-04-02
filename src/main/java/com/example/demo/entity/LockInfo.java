package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table(schema = "lock_info")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE DEMO.LOCK_INFO SET deleted = TRUE WHERE ID = ?")
@FilterDef(name = "deletedLockInfoFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedLockInfoFilter", condition = "deleted = :isDeleted")
public class LockInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    @NonNull
    private Long lockId;

    @Column(name = "lock_name", nullable = false)
    @NonNull
    private String lockName;

    @Column(name = "lock_state", nullable = false)
    @NonNull
    private Boolean lockState;

    @OneToMany(mappedBy = "lockInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            orphanRemoval = true, targetEntity = User.class)
    private Set<User> users = new HashSet<>();

    @Column(name = "deleted", nullable = false, columnDefinition = "bit default false")
    @NonNull
    private Boolean deleted = Boolean.FALSE;
}
