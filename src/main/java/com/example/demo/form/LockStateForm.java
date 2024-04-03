package com.example.demo.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LockStateForm {
    private List<Long> lockId;
}
