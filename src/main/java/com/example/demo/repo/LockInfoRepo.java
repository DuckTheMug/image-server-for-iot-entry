package com.example.demo.repo;

import com.example.demo.entity.LockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockInfoRepo extends JpaRepository<LockInfo, Long> {

    @Query("select l from LockInfo l where l.lockName ilike %?1% and l.deleted = false")
    Optional<List<LockInfo>> findByLockNameIgnoreCase(String lockName);
}
