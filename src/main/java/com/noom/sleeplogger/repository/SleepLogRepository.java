package com.noom.sleeplogger.repository;

import com.noom.sleeplogger.entity.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SleepLogRepository extends JpaRepository<SleepLog, UUID> {
    // 1. latest sleep log
    Optional<SleepLog> findTopByUserIdOrderBySleepDateDesc(UUID userId);

    // 2. last 30 days logs
    @Query("""
        SELECT s FROM SleepLog s
        WHERE s.userId = :userId
        AND s.sleepDate >= :fromDate
    """)
    List<SleepLog> findLast30Days(
            @Param("userId") UUID userId,
            @Param("fromDate") LocalDate fromDate
    );
}
