package com.commit.viewer.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.commit.viewer.services.DeveloperService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DeveloperController {
    private final DeveloperService developerService;

    @GetMapping("/developers")
    public String getAllDevelopers(Model model) {
        model.addAttribute("developers", developerService.getAllDevelopers());
        return "developers";
    }
}
