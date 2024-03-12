package com.example.demo.model;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(schema = "master")
@SQLDelete(sql = "UPDATE DEMO.MASTER SET DELETED = TRUE WHERE ID = ?")
@FilterDef(name = "deletedMasterFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedMasterFilter", condition = "deleted = :isDeleted")
public class Master {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	@NonNull
	private Long id;
	
	@Column(name = "username", nullable = false, unique = true)
	@NonNull
	private String username;
	
	@Column(name = "password", nullable = false)
	@NonNull
	private String password;
	
	@Column(name = "email", nullable = false, unique = true)
	@NonNull
	private String email;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(joinColumns = @JoinColumn(referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
	private Set<Role> role;

	@Column(name = "deleted", nullable = false)
	@NonNull
	private Boolean deleted = Boolean.FALSE;
}
