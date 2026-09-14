package com.carrental.reservation;

import java.time.LocalDateTime;

/**
 * ValueObject representing a rental period
 */
public record RentalPeriod(LocalDateTime startDateTime, int durationDays) {
    public RentalPeriod {
        if (startDateTime == null) {
            throw new IllegalArgumentException("Start date time cannot be null");
        }
        if (durationDays <= 0) {
            throw new IllegalArgumentException("Duration days must be greater than zero");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
    }
}

