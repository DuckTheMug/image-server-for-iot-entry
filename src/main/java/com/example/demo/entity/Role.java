package com.example.demo.entity;

import com.example.demo.constant.RoleName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(schema = "role")
@SQLDelete(sql = "UPDATE DEMO.ROLE SET DELETED = true WHERE ID = ?")
@FilterDef(name = "deletedRoleFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedRoleFilter", condition = "deleted = :isDeleted")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	@NonNull
	private Long roleId;
	
	@Column(name = "name", nullable = false, unique = true)
	@NonNull
	@Enumerated(EnumType.STRING)
	private RoleName name;

	@Column(name = "deleted", nullable = false)
	@NonNull
	private Boolean deleted = Boolean.FALSE;
}
