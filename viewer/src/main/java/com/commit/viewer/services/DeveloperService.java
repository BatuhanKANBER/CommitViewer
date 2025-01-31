package com.commit.viewer.services;

import org.springframework.stereotype.Service;

import com.commit.viewer.models.Developer;
import com.commit.viewer.repositories.DeveloperRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeveloperService {
    private final DeveloperRepository developerRepository;

    public void save(String username, String email) {
        Developer developer = new Developer();
        developer.setUsername(username);
        developer.setEmail(email);
        developerRepository.saveAndFlush(developer);
    }

    public Developer getDeveloper(String email) {
        return developerRepository.findByEmail(email);
    }
}
