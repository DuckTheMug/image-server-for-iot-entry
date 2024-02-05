package com.example.demo.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Master;
import com.example.demo.repo.MasterRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final MasterRepo masterRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Master master = masterRepo.findByUsername(email);
		if (master == null) {
			throw new UsernameNotFoundException("Can't find the user.");
		}
		Set<GrantedAuthority> auth = master.getRole().stream().map(role
				-> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toSet());
		return new User(master.getEmail(), master.getPassword(), auth);
	}
}
