package com.noom.sleeplogger.repository;

import com.noom.sleeplogger.entity.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SleepLogRepository extends JpaRepository<SleepLog, UUID> {
}
