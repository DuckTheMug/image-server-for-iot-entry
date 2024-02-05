package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterLoginDto {
	@NotEmpty
	@NonNull
	private String email;
	
	@NotEmpty
	@NonNull
	private String password;
}
