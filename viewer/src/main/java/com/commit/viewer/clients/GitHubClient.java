package com.commit.viewer.clients;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GitHubClient {

        @GetMapping("/repos/{owner}/{repo}/commits")
        List<Map<String, Object>> getCommits(
                        @PathVariable String owner,
                        @PathVariable String repo,
                        @RequestParam(value = "since", required = false) String since);

        @GetMapping("/repos/{owner}/{repo}/commits/{sha}")
        Map<String, Object> getCommitDetails(
                        @PathVariable String owner,
                        @PathVariable String repo,
                        @PathVariable String sha);
}