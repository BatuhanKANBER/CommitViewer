package com.commit.viewer.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.commit.viewer.models.Commit;
import com.commit.viewer.services.CommitService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommitController {
    private final CommitService commitService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/fetch/{owner}/{repo}")
    public String fetchCommits(@PathVariable String owner, @PathVariable String repo,
            RedirectAttributes redirectAttributes) {
        try {
            commitService.fetchAndSaveCommits(owner, repo);
            redirectAttributes.addFlashAttribute("successMessage", "Fetch and save operations successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "API rate limit exceeded.");
        }
        return "redirect:/";
    }

    @GetMapping("/commits/{username}")
    public String getDeveloperCommits(@PathVariable String username, Model model) {
        List<Commit> commits = commitService.getDeveloperCommits(username);
        model.addAttribute("commits", commits);
        return "commits";
    }

    @GetMapping("/commit/{id}")
    public String getCommit(@PathVariable Long id, Model model) {
        Commit commit = commitService.getCommit(id);
        model.addAttribute("commit", commit);
        return "commit_details";
    }
}
