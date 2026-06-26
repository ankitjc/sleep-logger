package com.noom.sleeplogger.service;

import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.entity.User;
import com.noom.sleeplogger.enums.Feeling;
import com.noom.sleeplogger.repository.SleepLogRepository;
import com.noom.sleeplogger.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
}