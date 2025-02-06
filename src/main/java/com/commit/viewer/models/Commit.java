package com.commit.viewer.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "commits")
@NoArgsConstructor
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String hash;

    String message;

    String committer;

    String author;

    LocalDateTime timestamp;

    @Lob
    String patch;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    Developer developer;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    Provider provider;

    public Commit(String hash, String message, String comitter, String author, LocalDateTime timestamp,
            Developer developer, Provider provider) {
        this.hash = hash;
        this.message = message;
        this.committer = comitter;
        this.author = author;
        this.timestamp = timestamp;
        this.developer = developer;
        this.provider = provider;
    }
}