package com.noom.sleeplogger.service;

import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.dto.response.SleepLogResponse;
import com.noom.sleeplogger.entity.SleepLog;
import com.noom.sleeplogger.repository.SleepLogRepository;
import com.noom.sleeplogger.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class SleepLogServiceImpl implements SleepLogService {

    private final SleepLogRepository sleepLogRepository;
    private final UserRepository userRepository;

    public SleepLogServiceImpl(SleepLogRepository sleepLogRepository,
                               UserRepository userRepository) {
        this.sleepLogRepository = sleepLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SleepLogResponse createSleepLog(UUID userId, CreateSleepLogRequest request) {

        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        long minutes = Duration.between(
                request.bedTime(),
                request.wakeTime()
        ).toMinutes();

        SleepLog log = new SleepLog();
        log.setUserId(userId);
        log.setSleepDate(request.sleepDate());
        log.setBedTime(request.bedTime());
        log.setWakeTime(request.wakeTime());
        log.setFeeling(request.feeling());
        log.setTotalTimeInBedMinutes((int) minutes);

        SleepLog saved = sleepLogRepository.save(log);

        return new SleepLogResponse(
                saved.getId(),
                saved.getSleepDate(),
                saved.getBedTime(),
                saved.getWakeTime(),
                saved.getTotalTimeInBedMinutes(),
                saved.getFeeling()
        );
    }
}