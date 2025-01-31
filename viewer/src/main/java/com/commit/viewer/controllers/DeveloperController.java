package com.commit.viewer.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.commit.viewer.models.Developer;
import com.commit.viewer.services.DeveloperService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DeveloperController {
    private final DeveloperService developerService;

    @GetMapping("/developers")
    public String getDevelopers(Model model) {
        List<Developer> developers = developerService.getDevelopers();
        model.addAttribute("developers", developers);
        return "developers";
    }
}
