package com.noom.sleeplogger.controller;

import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.dto.response.SleepLogResponse;
import com.noom.sleeplogger.service.SleepLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/sleep-logs")
public class SleepLogController {

    private final SleepLogService sleepLogService;

    public SleepLogController(SleepLogService sleepLogService) {
        this.sleepLogService = sleepLogService;
    }

    @PostMapping
    public ResponseEntity<SleepLogResponse> create(
            @PathVariable UUID userId,
            @RequestBody CreateSleepLogRequest request
    ) {
        return ResponseEntity.ok(
                sleepLogService.createSleepLog(userId, request)
        );
    }
}