package com.noom.sleeplogger.service;

import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.dto.response.SleepLogResponse;

import java.util.UUID;

public interface SleepLogService {

    SleepLogResponse createSleepLog(UUID userId, CreateSleepLogRequest request);
}