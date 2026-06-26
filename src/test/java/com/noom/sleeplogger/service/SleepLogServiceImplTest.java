package com.noom.sleeplogger.service;

import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.entity.SleepLog;
import com.noom.sleeplogger.entity.User;
import com.noom.sleeplogger.enums.Feeling;
import com.noom.sleeplogger.repository.SleepLogRepository;
import com.noom.sleeplogger.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SleepLogServiceImplTest {

    private final SleepLogRepository sleepLogRepository = mock(SleepLogRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    private final SleepLogServiceImpl service =
            new SleepLogServiceImpl(sleepLogRepository, userRepository);

    @Test
    void should_create_sleep_log_successfully() {

        UUID userId = UUID.randomUUID();

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("TestUser");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(mockUser));

        CreateSleepLogRequest request = new CreateSleepLogRequest(
                LocalDate.of(2026, 1, 1),
                LocalDateTime.of(2026, 1, 1, 22, 0),
                LocalDateTime.of(2026, 1, 2, 6, 0),
                Feeling.GOOD
        );

        when(sleepLogRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.createSleepLog(userId, request);

        assertThat(response.sleepDate()).isEqualTo(LocalDate.of(2026, 1, 1));
        assertThat(response.totalTimeInBedMinutes()).isEqualTo(480);
        assertThat(response.feeling()).isEqualTo(Feeling.GOOD);

        verify(userRepository, times(1)).findById(userId);
        verify(sleepLogRepository, times(1)).save(any());
    }

    @Test
    void should_throw_exception_when_user_not_found() {

        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        CreateSleepLogRequest request = new CreateSleepLogRequest(
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(8),
                Feeling.OK
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.createSleepLog(userId, request));

        verify(sleepLogRepository, never()).save(any());
    }

    @Test
    void should_return_latest_sleep_log() {

        UUID userId = UUID.randomUUID();

        SleepLog log = new SleepLog();
        log.setId(UUID.randomUUID());
        log.setUserId(userId);
        log.setTotalTimeInBedMinutes(420);

        when(sleepLogRepository.findTopByUserIdOrderBySleepDateDesc(userId))
                .thenReturn(Optional.of(log));

        var result = service.getLatestSleepLog(userId);

        assertThat(result.totalTimeInBedMinutes()).isEqualTo(420);
    }

    @Test
    void should_calculate_30_day_summary() {

        UUID userId = UUID.randomUUID();

        SleepLog log1 = new SleepLog();
        log1.setTotalTimeInBedMinutes(400);
        log1.setBedTime(LocalDateTime.of(2026,1,1,22,0));
        log1.setWakeTime(LocalDateTime.of(2026,1,2,6,0));
        log1.setFeeling(Feeling.GOOD);

        SleepLog log2 = new SleepLog();
        log2.setTotalTimeInBedMinutes(500);
        log2.setBedTime(LocalDateTime.of(2026,1,2,23,0));
        log2.setWakeTime(LocalDateTime.of(2026,1,3,7,0));
        log2.setFeeling(Feeling.OK);

        when(sleepLogRepository.findLast30Days(any(), any()))
                .thenReturn(List.of(log1, log2));

        var result = service.getLast30DaySummary(userId);

        assertThat(result.averageTimeInBedMinutes()).isEqualTo(450);
        assertThat(result.feelingFrequency().get(Feeling.GOOD)).isEqualTo(1);
        assertThat(result.feelingFrequency().get(Feeling.OK)).isEqualTo(1);
    }
}