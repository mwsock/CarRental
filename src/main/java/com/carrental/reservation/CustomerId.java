package com.carrental.reservation;

/**
 * ValueObject representing a unique customer identifier
 */
public record CustomerId(String value) {
    public CustomerId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
    }
}
