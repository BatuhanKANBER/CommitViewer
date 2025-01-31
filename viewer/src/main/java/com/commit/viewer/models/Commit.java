package com.commit.viewer.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "commits")
public class Commit {
    @Id
    String hash;

    String message;

    String author;

    LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    Developer developer;
}
