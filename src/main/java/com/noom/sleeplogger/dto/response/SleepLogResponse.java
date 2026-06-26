package com.noom.sleeplogger.dto.response;

import com.noom.sleeplogger.enums.Feeling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SleepLogResponse(
        UUID id,
        LocalDate sleepDate,
        LocalDateTime bedTime,
        LocalDateTime wakeTime,
        Integer totalTimeInBedMinutes,
        Feeling feeling
) {}