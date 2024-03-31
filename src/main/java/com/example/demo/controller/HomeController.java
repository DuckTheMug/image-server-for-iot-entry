package com.example.demo.controller;

import com.example.demo.constant.AccessPathConstants;
import com.example.demo.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
   private final EntryService entryService;

    @GetMapping("/master")
    public String home() {
        return AccessPathConstants.HOME;
    }

    @GetMapping("/")
    public String redirectHome() {
        return AccessPathConstants.REDIRECT_HOME;
    }
}
