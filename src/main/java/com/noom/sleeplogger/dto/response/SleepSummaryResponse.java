package com.noom.sleeplogger.dto.response;

import com.noom.sleeplogger.enums.Feeling;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public record SleepSummaryResponse(
        LocalDate from,
        LocalDate to,
        Double averageTimeInBedMinutes,
        LocalTime averageBedTime,
        LocalTime averageWakeTime,
        Map<Feeling, Long> feelingFrequency
) {}