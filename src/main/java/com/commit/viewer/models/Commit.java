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

@Data
@Entity
@Table(name = "commits")
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
}