package com.noom.sleeplogger.exception;

import java.util.UUID;

public class SleepLogNotFoundException extends RuntimeException {
    public SleepLogNotFoundException(String message) {
        super(message);
    }

    public SleepLogNotFoundException(UUID userId) {
        super("No sleep logs found for user: " + userId);
    }
}