package com.commit.viewer.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.commit.viewer.models.Commit;
import com.commit.viewer.models.Developer;
import com.commit.viewer.repositories.CommitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommitService {
    private final GitHubClient gitHubClient;
    private final CommitRepository commitRepository;
    private final DeveloperService developerService;

    public void fetchAndSaveCommits(String owner, String repo) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        String since = oneMonthAgo.format(DateTimeFormatter.ISO_DATE_TIME);

        List<Map<String, Object>> commitsForHub = gitHubClient.getCommits(owner, repo, since);
        for (Map<String, Object> commitData : commitsForHub) {

            Map<String, Object> commitDetails = (Map<String, Object>) commitData.get("commit");
            Map<String, Object> authorDetails = (Map<String, Object>) commitDetails.get("author");
            Map<String, Object> userId = (Map<String, Object>) commitData.get("author");

            String username = ((String) userId.get("login"));
            String email = ((String) authorDetails.get("email"));
            if (developerService.getDeveloper(username) == null) {
                developerService.save(username, email);
            }

            Developer developer = developerService.getDeveloper(username);
            Commit commit = new Commit();
            commit.setHash((String) commitData.get("sha"));
            commit.setMessage((String) commitDetails.get("message"));
            commit.setAuthor(developer.getUsername());
            commit.setDeveloper(developer);
            commit.setTimestamp(
                    LocalDateTime.parse((String) authorDetails.get("date"), DateTimeFormatter.ISO_DATE_TIME));

            Map<String, Object> commitDetailsFromGitHub = gitHubClient.getCommitDetails(owner, repo,
                    (String) commitData.get("sha"));
            List<Map<String, Object>> files = (List<Map<String, Object>>) commitDetailsFromGitHub.get("files");
            commit.setPatch(getCommitPatchOnGitHub(files).toString());

            commitRepository.save(commit);
        }
    }

    public StringBuilder getCommitPatchOnGitHub(List<Map<String, Object>> files) {
        StringBuilder patchBuilder = new StringBuilder();
        for (Map<String, Object> file : files) {
            String patch = (String) file.get("patch");
            if (patch != null) {
                patchBuilder.append(patch).append("\n");
            }
        }
        return patchBuilder;
    }

    public List<Commit> getDeveloperCommits(String username) {
        Developer developer = developerService.getDeveloper(username);
        return commitRepository.findByDeveloper(developer);
    }

    public Commit getCommit(String hash) {
        return commitRepository.findByHash(hash);
    }
}
