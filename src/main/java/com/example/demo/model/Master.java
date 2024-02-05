package com.example.demo.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(schema = "master")
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
}
