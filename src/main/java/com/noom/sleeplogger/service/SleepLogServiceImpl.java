package com.noom.sleeplogger.service;

import com.noom.sleeplogger.dto.request.CreateSleepLogRequest;
import com.noom.sleeplogger.dto.response.SleepLogResponse;
import com.noom.sleeplogger.dto.response.SleepSummaryResponse;
import com.noom.sleeplogger.entity.SleepLog;
import com.noom.sleeplogger.enums.Feeling;
import com.noom.sleeplogger.repository.SleepLogRepository;
import com.noom.sleeplogger.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Override
    public SleepLogResponse getLatestSleepLog(UUID userId) {
        SleepLog log = sleepLogRepository
                .findTopByUserIdOrderBySleepDateDesc(userId)
                .orElseThrow(() -> new IllegalArgumentException("No sleep logs found"));

        return new SleepLogResponse(
                log.getId(),
                log.getSleepDate(),
                log.getBedTime(),
                log.getWakeTime(),
                log.getTotalTimeInBedMinutes(),
                log.getFeeling()
        );
    }

    @Override
    public SleepSummaryResponse getLast30DaySummary(UUID userId) {

        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(30);

        List<SleepLog> logs = sleepLogRepository.findLast30Days(userId, from);

        if (logs.isEmpty()) {
            throw new IllegalArgumentException("No data for last 30 days");
        }

        // avg time in bed
        double avgTimeInBed = logs.stream()
                .mapToInt(SleepLog::getTotalTimeInBedMinutes)
                .average()
                .orElse(0);

        // average bed time
        double avgBedMinutes = logs.stream()
                .mapToLong(l -> l.getBedTime().toLocalTime().toSecondOfDay())
                .average()
                .orElse(0);

        LocalTime avgBedTime = LocalTime.ofSecondOfDay((long) avgBedMinutes);

        // average wake time
        double avgWakeMinutes = logs.stream()
                .mapToLong(l -> l.getWakeTime().toLocalTime().toSecondOfDay())
                .average()
                .orElse(0);

        LocalTime avgWakeTime = LocalTime.ofSecondOfDay((long) avgWakeMinutes);

        // feeling frequency
        Map<Feeling, Long> frequency = logs.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        SleepLog::getFeeling,
                        java.util.stream.Collectors.counting()
                ));

        return new SleepSummaryResponse(
                from,
                to,
                avgTimeInBed,
                avgBedTime,
                avgWakeTime,
                frequency
        );
    }
}