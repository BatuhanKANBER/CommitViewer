package com.commit.viewer.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "developers")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "developer_id")
    Long id;

    String username;

    String email;

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Commit> commits;
}
