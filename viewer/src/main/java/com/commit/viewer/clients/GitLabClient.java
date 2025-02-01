package com.commit.viewer.clients;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gitLabClient", url = "https://gitlab.com/api/v4/projects")
public interface GitLabClient {

        @GetMapping("/{owner}%2F{repo}")
        Map<String, Object> getRepoId(
                        @PathVariable String owner,
                        @PathVariable String repo);

        @GetMapping("/{id}/repository/commits")
        List<Map<String, Object>> getCommits(
                        @PathVariable int id,
                        @RequestParam(value = "since", required = false) String since);

        @GetMapping("/{id}/repository/commits/{sha}/diff")
        List<Map<String, Object>> getCommitDetails(@PathVariable("id") int id,
                        @PathVariable("sha") String sha);
}
