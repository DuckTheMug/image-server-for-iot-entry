package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.repo.MasterRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MasterService {
	private final MasterRepo masterRepo;
	
	
}
