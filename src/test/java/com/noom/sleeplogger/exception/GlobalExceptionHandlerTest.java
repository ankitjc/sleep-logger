package com.noom.sleeplogger.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void shouldMapSleepLogNotFoundExceptionTo404() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiError> response =
                handler.handleSleepLogNotFound(new SleepLogNotFoundException("No data"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No data", Objects.requireNonNull(response.getBody()).message());
    }

    @Test
    void shouldMapUserNotFoundExceptionTo404() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        UUID mockUUID = UUID.randomUUID();
        ResponseEntity<ApiError> response =
                handler.handleUserNotFound(new UserNotFoundException(mockUUID));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found: " + mockUUID, Objects.requireNonNull(response.getBody()).message());
    }

    @Test
    void shouldMapIllegalArgumentExceptionToBadRequest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<ApiError> response =
                handler.handleBadRequest(new IllegalArgumentException("Bad request"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", Objects.requireNonNull(response.getBody()).message());
    }
}