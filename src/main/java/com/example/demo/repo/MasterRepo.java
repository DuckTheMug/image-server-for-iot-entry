package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Master;

@Repository
public interface MasterRepo extends JpaRepository<Master, Long>{
    Master findByUsername(String name);
}
