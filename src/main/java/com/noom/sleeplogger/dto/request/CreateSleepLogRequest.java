package com.noom.sleeplogger.dto.request;

import com.noom.sleeplogger.enums.Feeling;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateSleepLogRequest(
        LocalDate sleepDate,
        LocalDateTime bedTime,
        LocalDateTime wakeTime,
        Feeling feeling
) {}