package com.commit.viewer.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.commit.viewer.clients.GitHubClient;
import com.commit.viewer.clients.GitLabClient;
import com.commit.viewer.models.Commit;
import com.commit.viewer.models.Developer;
import com.commit.viewer.models.Provider;
import com.commit.viewer.repositories.CommitRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommitService {
    private final GitHubClient gitHubClient;
    private final GitLabClient gitLabClient;
    private final CommitRepository commitRepository;
    private final DeveloperService developerService;

    public void fetchAndSaveCommits(String owner, String repo) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        String since = oneMonthAgo.format(DateTimeFormatter.ISO_DATE_TIME);
        Map<String, Object> repoId = gitLabClient.getRepoId(owner, repo);
        int id = 0;
        if (repoId.containsKey("id")) {
            id = (int) repoId.get("id");
        }

        // GitLab Commits
        List<Map<String, Object>> commitsForLab = gitLabClient.getCommits(id, since);
        for (Map<String, Object> commitData1 : commitsForLab) {
            String hash1 = (String) commitData1.get("id");
            String message1 = (String) commitData1.get("message");
            String author1 = (String) commitData1.get("author_name");
            String username1 = (String) commitData1.get("committer_name");
            String email1 = (String) commitData1.get("committer_email");

            if (developerService.getDeveloper(username1) == null) {
                developerService.save(username1, email1);
            }

            Developer developer1 = developerService.getDeveloper(username1);
            Commit commit1 = new Commit();
            commit1.setHash(hash1);
            commit1.setMessage(message1);
            commit1.setCommitter(developer1.getUsername());
            commit1.setAuthor(author1);
            commit1.setDeveloper(developer1);
            commit1.setTimestamp(
                    LocalDateTime.parse((String) commitData1.get("committed_date"),
                            DateTimeFormatter.ISO_DATE_TIME));
            commit1.setProvider(Provider.GITLAB);

            List<Map<String, Object>> commitDetailsFromGitLab = gitLabClient.getCommitDetails(id,
                    (String) commitData1.get("id"));
            if (commitDetailsFromGitLab != null) {
                commit1.setPatch(getCommitPatchFromGitLab(commitDetailsFromGitLab).toString());
            }

            commitRepository.save(commit1);
        }

        // GitHub Commits
        List<Map<String, Object>> commitsForHub = gitHubClient.getCommits(owner, repo, since);
        for (Map<String, Object> commitData : commitsForHub) {
            Map<String, Object> commitDetails = (Map<String, Object>) commitData.get("commit");
            Map<String, Object> authorDetails = (Map<String, Object>) commitDetails.get("author");
            Map<String, Object> developerDetails = (Map<String, Object>) commitDetails.get("committer");

            String hash = (String) commitData.get("sha");
            String message = (String) commitDetails.get("message");
            String author = (String) authorDetails.get("name");
            String username = (String) developerDetails.get("name");
            String email = (String) developerDetails.get("email");

            if (developerService.getDeveloper(username) == null) {
                developerService.save(username, email);
            }

            Developer developer = developerService.getDeveloper(username);
            Commit commit = new Commit();
            commit.setHash(hash);
            commit.setMessage(message);
            commit.setCommitter(developer.getUsername());
            commit.setAuthor(author);
            commit.setDeveloper(developer);
            commit.setTimestamp(
                    LocalDateTime.parse((String) developerDetails.get("date"), DateTimeFormatter.ISO_DATE_TIME));
            commit.setProvider(Provider.GITHUB);

            Map<String, Object> commitDetailsFromGitHub = gitHubClient.getCommitDetails(owner, repo,
                    (String) commitData.get("sha"));
            if (commitDetailsFromGitHub != null && commitDetailsFromGitHub.containsKey("files")) {
                List<Map<String, Object>> files = (List<Map<String, Object>>) commitDetailsFromGitHub.get("files");
                commit.setPatch(getCommitPatchFromGitHub(files).toString());
            }

            commitRepository.save(commit);
        }

    }

    public StringBuilder getCommitPatchFromGitHub(List<Map<String, Object>> files) {
        StringBuilder patchBuilder = new StringBuilder();
        for (Map<String, Object> file : files) {
            String patch = (String) file.get("patch");
            if (patch != null) {
                patchBuilder.append(patch).append("\n");
            }
        }
        return patchBuilder;
    }

    public StringBuilder getCommitPatchFromGitLab(List<Map<String, Object>> commitDetailsFromGitLab) {

        StringBuilder patchBuilder = new StringBuilder();
        for (Map<String, Object> commitDetail : commitDetailsFromGitLab) {
            if (commitDetail.containsKey("diff")) {
                patchBuilder.append(commitDetail.get("diff")).append("\n");
            }
        }
        return patchBuilder;
    }

    public List<Commit> getDeveloperCommits(String username) {
        Developer developer = developerService.getDeveloper(username);
        return commitRepository.findByDeveloper(developer);
    }

    public Commit getCommit(Long id) {
        return commitRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }
}
