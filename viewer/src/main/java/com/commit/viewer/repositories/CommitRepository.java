package com.commit.viewer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commit.viewer.models.Commit;
import com.commit.viewer.models.Developer;

public interface CommitRepository extends JpaRepository<Commit, String> {
        List<Commit> findByDeveloper(Developer developer);

        Commit findByHash(String hash);
}
