package com.noom.sleeplogger.entity;

import com.noom.sleeplogger.enums.Feeling;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "sleep_logs",
        indexes = {
                @Index(name = "idx_sleep_logs_user_date", columnList = "user_id, sleep_date"),
                @Index(name = "idx_sleep_logs_user_created", columnList = "user_id, created_at")
        }
)
public class SleepLog {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "sleep_date", nullable = false)
    private LocalDate sleepDate;

    @Column(name = "bed_time", nullable = false)
    private LocalDateTime bedTime;

    @Column(name = "wake_time", nullable = false)
    private LocalDateTime wakeTime;

    @Column(name = "total_time_in_bed_minutes", nullable = false)
    private Integer totalTimeInBedMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Feeling feeling;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public SleepLog() {}

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}