package com.commit.viewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commit.viewer.models.Commit;

public interface CommitRepository extends JpaRepository<Commit, String> {

}
