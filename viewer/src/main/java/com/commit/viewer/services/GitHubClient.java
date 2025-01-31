package com.commit.viewer.services;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GitHubClient {
    
    @GetMapping("/repos/{owner}/{repo}/commits")
    List<Map<String, Object>> getCommits(
            @RequestParam("owner") String owner, 
            @RequestParam("repo") String repo,
            @RequestParam("since") String since
    );
}