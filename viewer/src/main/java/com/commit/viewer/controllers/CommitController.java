package com.commit.viewer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.commit.viewer.services.CommitService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommitController {
    private final CommitService commitService;

    @PostMapping("/fetch/{owner}/{repo}")
    public String fetchCommits(@PathVariable String owner, @PathVariable String repo) {
        commitService.fetchAndSaveCommits(owner, repo);
        return "Commits successfully fetched and saved.";
    }
}
