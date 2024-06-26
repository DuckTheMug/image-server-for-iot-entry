package com.example.demo.controller;

import com.example.demo.constant.AccessPathConstants;
import com.example.demo.dto.AjaxResponseDto.CommonAjaxResponseDto;
import com.example.demo.dto.LockInfoDto;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.form.LockStateForm;
import com.example.demo.form.LockForm;
import com.example.demo.form.LockUpdateForm;
import com.example.demo.service.LockInfoService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/master/lock")
public class LockInfoController {
    private final LockInfoService lockInfoService;
    @PostMapping("/doGetAllLockName")
    public @ResponseBody CommonAjaxResponseDto doGetAllLockName() {
        Map<String, List<String>> resultMap = new HashMap<>();
        resultMap.put("locks", lockInfoService.getAllLocks());
        return CommonAjaxResponseDto.success(null, resultMap);
    }

    @PostMapping("/doSearch")
    public @ResponseBody CommonAjaxResponseDto doSearch(@NonNull LockForm lockForm) {
        Map<String, List<LockInfoDto>> resultMap = new HashMap<>();
        resultMap.put("locks", lockInfoService.findByLockNameIgnoreCase(lockForm.getLockName()));
        return CommonAjaxResponseDto.success(null, resultMap);
    }

    @PostMapping("/doAdd")
    public @ResponseBody CommonAjaxResponseDto doInsertNewLock(@NonNull LockForm lockForm) {
        try {
            lockInfoService.insertNewLock(lockForm.getLockName());
            return CommonAjaxResponseDto.success("Lock created.");
        } catch (DuplicationException e) {
            return CommonAjaxResponseDto.error(e.getMessage());
        }
    }

    @PostMapping("/doDelete")
    public @ResponseBody CommonAjaxResponseDto doDeleteLock(@NonNull LockStateForm lockForm) {
        lockInfoService.deleteLock(lockForm.getLockId());
        return CommonAjaxResponseDto.success("Lock deleted.");
    }

    @PostMapping("/doUpdateLock")
    public @ResponseBody CommonAjaxResponseDto doUpdateLock(@NonNull LockUpdateForm lockForm) {
        try {
            lockInfoService.updateLockName(lockForm.getLockId(), lockForm.getLockName());
            return CommonAjaxResponseDto.success("Lock updated.");
        } catch (NotFoundException e) {
            return CommonAjaxResponseDto.error(e.getMessage());
        }
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String init() {
        return AccessPathConstants.LOCK;
    }
}
