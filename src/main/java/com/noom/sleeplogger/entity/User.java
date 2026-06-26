package com.noom.sleeplogger.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public User() {}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}