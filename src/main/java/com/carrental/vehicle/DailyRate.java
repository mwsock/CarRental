package com.carrental.vehicle;

import java.math.BigDecimal;

/**
 * ValueObject representing a daily rental rate
 */
public record DailyRate(BigDecimal amount) {
    public DailyRate {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Daily rate must be greater than zero");
        }
    }

    public BigDecimal calculateTotalRate(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Number of days must be greater than zero");
        }
        return amount.multiply(BigDecimal.valueOf(days));
    }
}

