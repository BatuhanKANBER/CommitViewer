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
        try {
            fetchAndSaveGitLabCommits(owner, repo, since);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fetchAndSaveGitHubCommits(owner, repo, since);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fetchAndSaveGitHubCommits(String owner, String repo, String since) {
        List<Map<String, Object>> commitsForHub = gitHubClient.getCommits(owner, repo, since);
        for (Map<String, Object> commitData : commitsForHub) {
            String hash = (String) commitData.get("sha");
            if (commitRepository.existsByHash(hash)) {
                continue;
            }
            Map<String, Object> commitDetails = (Map<String, Object>) commitData.get("commit");
            Map<String, Object> authorDetails = (Map<String, Object>) commitDetails.get("author");
            Map<String, Object> developerDetails = (Map<String, Object>) commitDetails.get("committer");

            String message = (String) commitDetails.get("message");
            String author = (String) authorDetails.get("name");
            String username = (String) developerDetails.get("name");
            String email = (String) developerDetails.get("email");
            LocalDateTime timestamp = LocalDateTime.parse((String) developerDetails.get("date"),
                    DateTimeFormatter.ISO_DATE_TIME);
            Developer developer = getOrSaveDeveloper(username, email);

            Commit commit = buildCommit(commitData, developer, hash, message, author, timestamp, Provider.GITHUB);
            Map<String, Object> commitDetailsFromGitHub = gitHubClient.getCommitDetails(owner, repo, hash);

            if (commitDetailsFromGitHub != null && commitDetailsFromGitHub.containsKey("files")) {
                List<Map<String, Object>> files = (List<Map<String, Object>>) commitDetailsFromGitHub.get("files");
                commit.setPatch(getCommitPatchFromGitHub(files).toString());
            }

            commitRepository.save(commit);
        }
    }

    private void fetchAndSaveGitLabCommits(String owner, String repo, String since) {
        Map<String, Object> repoId = gitLabClient.getRepoId(owner, repo);
        if (!repoId.containsKey("id"))
            return;

        int id = (int) repoId.get("id");
        List<Map<String, Object>> commitsForLab = gitLabClient.getCommits(id, since);
        for (Map<String, Object> commitData : commitsForLab) {
            String hash = (String) commitData.get("id");
            if (commitRepository.existsByHash(hash)) {
                continue;
            }
            String message = (String) commitData.get("message");
            String author = (String) commitData.get("author_name");
            String username = (String) commitData.get("committer_name");
            String email = (String) commitData.get("committer_email");
            LocalDateTime timestamp = LocalDateTime.parse((String) commitData.get("committed_date"),
                    DateTimeFormatter.ISO_DATE_TIME);
            Developer developer = getOrSaveDeveloper(username, email);

            Commit commit = buildCommit(commitData, developer, hash, message, author, timestamp, Provider.GITLAB);
            List<Map<String, Object>> commitDetailsFromGitLab = gitLabClient.getCommitDetails(id, hash);

            if (commitDetailsFromGitLab != null) {
                commit.setPatch(getCommitPatchFromGitLab(commitDetailsFromGitLab).toString());
            }
            commitRepository.save(commit);
        }
    }

    private Developer getOrSaveDeveloper(String username, String email) {
        Developer developer = developerService.getDeveloper(username);
        if (developer == null) {
            developerService.save(username, email);
            developer = developerService.getDeveloper(username);
        }
        return developer;
    }

    private Commit buildCommit(Map<String, Object> commitData, Developer developer, String hash, String message,
            String author, LocalDateTime timestamp, Provider provider) {
        Commit commit = new Commit();
        commit.setHash(hash);
        commit.setMessage(message);
        commit.setCommitter(developer.getUsername());
        commit.setAuthor(author);
        commit.setDeveloper(developer);
        commit.setTimestamp(timestamp);
        commit.setProvider(provider);
        return commit;
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
