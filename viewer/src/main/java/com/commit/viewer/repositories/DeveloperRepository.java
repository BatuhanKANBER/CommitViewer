package com.commit.viewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commit.viewer.models.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Developer findByUsername(String username);
}
