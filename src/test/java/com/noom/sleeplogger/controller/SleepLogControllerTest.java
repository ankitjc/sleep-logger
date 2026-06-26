package com.noom.sleeplogger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.dto.response.SleepLogResponse;
import com.noom.sleeplogger.dto.response.SleepSummaryResponse;
import com.noom.sleeplogger.enums.Feeling;
import com.noom.sleeplogger.service.SleepLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SleepLogController.class)
class SleepLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SleepLogService sleepLogService;

    @Test
    void should_create_sleep_log_and_return_200() throws Exception {

        UUID userId = UUID.randomUUID();

        CreateSleepLogRequest request = new CreateSleepLogRequest(
                LocalDate.of(2026, 1, 1),
                LocalDateTime.of(2026, 1, 1, 22, 0),
                LocalDateTime.of(2026, 1, 2, 6, 0),
                Feeling.GOOD
        );

        SleepLogResponse response = new SleepLogResponse(
                UUID.randomUUID(),
                request.sleepDate(),
                request.bedTime(),
                request.wakeTime(),
                480,
                Feeling.GOOD
        );

        when(sleepLogService.createSleepLog(userId, request))
                .thenReturn(response);

        mockMvc.perform(post("/users/{userId}/sleep-logs", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTimeInBedMinutes").value(480))
                .andExpect(jsonPath("$.feeling").value("GOOD"));
    }

    @Test
    void should_return_latest_sleep_log() throws Exception {

        UUID userId = UUID.randomUUID();

        SleepLogResponse response = new SleepLogResponse(
                UUID.randomUUID(),
                LocalDate.of(2026, 1, 1),
                LocalDateTime.of(2026, 1, 1, 22, 0),
                LocalDateTime.of(2026, 1, 2, 6, 0),
                480,
                Feeling.GOOD
        );

        when(sleepLogService.getLatestSleepLog(userId))
                .thenReturn(response);

        mockMvc.perform(get("/users/{userId}/sleep-logs/latest", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTimeInBedMinutes").value(480))
                .andExpect(jsonPath("$.feeling").value("GOOD"))
                .andExpect(jsonPath("$.sleepDate").value("2026-01-01"));
    }

    @Test
    void should_return_30_day_summary() throws Exception {

        UUID userId = UUID.randomUUID();

        SleepSummaryResponse response = new SleepSummaryResponse(
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2026, 1, 1),
                450.0,
                LocalTime.of(22, 30),
                LocalTime.of(6, 30),
                Map.of(
                        Feeling.GOOD, 10L,
                        Feeling.OK, 5L,
                        Feeling.BAD, 2L
                )
        );

        when(sleepLogService.getLast30DaySummary(userId))
                .thenReturn(response);

        mockMvc.perform(get("/users/{userId}/sleep-logs/summary", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageTimeInBedMinutes").value(450.0))
                .andExpect(jsonPath("$.feelingFrequency.GOOD").value(10))
                .andExpect(jsonPath("$.feelingFrequency.OK").value(5))
                .andExpect(jsonPath("$.feelingFrequency.BAD").value(2));
    }
}