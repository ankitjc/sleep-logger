package com.noom.sleeplogger.exception;

import java.time.Instant;

public record ApiError(
        String message,
        int status,
        Instant timestamp
) {}
