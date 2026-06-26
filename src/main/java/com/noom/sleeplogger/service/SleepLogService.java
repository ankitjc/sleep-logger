package com.noom.sleeplogger.service;

import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.dto.response.SleepLogResponse;
import com.noom.sleeplogger.dto.response.SleepSummaryResponse;

import java.util.Optional;
import java.util.UUID;

public interface SleepLogService {

    SleepLogResponse createSleepLog(UUID userId, CreateSleepLogRequest request);

    SleepLogResponse getLatestSleepLog(UUID userId);

    SleepSummaryResponse getLast30DaySummary(UUID userId);
}