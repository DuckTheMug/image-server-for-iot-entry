package com.example.demo.service;

import com.example.demo.dto.LockInfoDto;
import com.example.demo.entity.LockInfo;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repo.LockInfoRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class LockInfoService {
    private final LockInfoRepo lockInfoRepo;
    private final ModelMapper modelMapper;

    public void setLockState(@NonNull Long lockId, @NonNull Boolean state) {
        try {
            LockInfo lockInfo = Objects.requireNonNull(lockInfoRepo.findById(lockId).orElse(null));
            lockInfo.setLockState(state);
            lockInfoRepo.save(lockInfo);
        } catch (NullPointerException e) {
            throw new NotFoundException("Lock not found");
        }
    }

    public Boolean getLockState(@NonNull Long lockId) {
        try {
            return Objects.requireNonNull(lockInfoRepo.findById(lockId).orElse(null)).getLockState();
        } catch (NullPointerException e) {
            throw new NotFoundException("Lock not found");
        }
    }

    public void flipLockState(@NonNull Long lockId) {
        try {
            LockInfo lockInfo = Objects.requireNonNull(lockInfoRepo.findById(lockId).orElse(null));
            lockInfo.setLockState(!lockInfo.getLockState());
            lockInfoRepo.save(lockInfo);
        } catch (NullPointerException e) {
            throw new NotFoundException("Lock not found");
        }
    }

    public void insertNewLock(@NonNull String lockName) {
        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockName(lockName);
        lockInfo.setLockState(false);
        lockInfoRepo.save(lockInfo);
    }

    public void deleteLock(@NonNull Long lockId) {
        lockInfoRepo.deleteById(lockId);
    }

    public void updateLockName(@NonNull Long lockId, @NonNull String lockName, Boolean state) throws NotFoundException {
        LockInfo lockInfo = lockInfoRepo.findById(lockId).orElse(null);
        if (lockInfo != null) {
            lockInfo.setLockName(lockName);
            if (state != null) {
                lockInfo.setLockState(state);
            }
            lockInfoRepo.save(lockInfo);
        } else {
            throw new NotFoundException("Lock not found");
        }
    }

    public List<String> getAllLocks() {
        return lockInfoRepo.findAll().stream().map(LockInfo::getLockName).toList();
    }

    public List<LockInfoDto> findByLockNameIgnoreCase(String lockName) {
        try {
            return Objects.requireNonNull(lockInfoRepo.findByLockNameIgnoreCase(lockName).orElse(null)).stream().map((element) -> modelMapper.map(element, LockInfoDto.class)).toList();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
