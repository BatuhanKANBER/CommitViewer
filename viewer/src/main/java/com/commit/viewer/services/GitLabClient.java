package com.commit.viewer.services;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gitLabClient", url = "https://gitlab.com/api/v4/projects/")
public interface GitLabClient {

    @GetMapping("{owner}/{repo}")
    Map<String, Object> getGitLabProjectId(
            @RequestParam("owner") String owner,
            @RequestParam("repo") String repo);
}
